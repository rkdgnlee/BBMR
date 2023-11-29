package com.example.bbmr_project.Dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.bbmr_project.CartStorage
import com.example.bbmr_project.Product
import com.example.bbmr_project.VO.Senior_TakeOutVO
import com.example.bbmr_project.databinding.DialogSeniorMenuBinding

class SeniorMenuDialog : DialogFragment() {
    var buttonDoubleDefend = false
    private lateinit var binding: DialogSeniorMenuBinding

    companion object {
        fun SeniorMenu(item: Senior_TakeOutVO): SeniorMenuDialog {
            val args = Bundle().apply {
                putString("sname", item.sname)
                putInt("sprice", item.sprice)
//                putInt("simg", item.simg)
            }
            val fragment = SeniorMenuDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        val darkTransparentBlack = Color.argb((255 * 0.6).toInt(), 0, 0, 0)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(darkTransparentBlack))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setDimAmount(0.4f)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSeniorMenuBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val seniorTakeoutVO = arguments?.getParcelable<Senior_TakeOutVO>("seniorTakeOutVO")
        // 메뉴 선택시 Dialog에 메뉴의 기본정보 제공하는 코드 (Adapter에서 받아온 값을 화면에 보여주기 위한 코드)
        seniorTakeoutVO?.let {item ->
            var text = item.sname
            // ------ 각 메뉴의 이름들의 길이에 조건을 걸어 줄 바꿈 해주기 ------
            val spannableStringBuilder = SpannableStringBuilder(text)
            if (text.length >= 6) {
                val indexLine = text.indexOf(' ')
                if (indexLine != -1) {
                    val modiText = StringBuilder(text)
                        .replace(indexLine, indexLine+1, "\n")
                        .toString()
                    spannableStringBuilder.replace(0, text.length, modiText)
                    binding.tvMenuName.text = spannableStringBuilder
                }
            } else if (text.length < 6 && !text.contains(' ')) {
                binding.tvMenuName.text = item.sname
            }

            binding.tvMenuPrice.text =
                String.format("%,d원", item.sprice) // String.format("%,d", 값) -> 1000 단위마다 , 표시
            Glide.with(requireContext()).load(item.simg).into(binding.imgMenu)
        }

        // ------ 추가 옵션 이동 코드 시작 ------
        binding.btnAddtionOption.setOnClickListener {
            // -- 연속클릭 방지 -- //
            if (!buttonDoubleDefend) {
                buttonDoubleDefend = true

                val product = Product(
                    binding.tvMenuName.text.toString(),
                    price = binding.tvMenuPrice.text.toString().replace(",", "").replace(" 원", "")
                        .toIntOrNull() ?: 0,
                    binding.tvMenuCount.text.toString().toInt()
                )
                val dialogFragment = Senior_AdditionalOptionDialog()
                val bundle = Bundle()
                bundle.putSerializable("product_option", product)
                dialogFragment.arguments = bundle
                dialogFragment.show(childFragmentManager, "Senior_AdditionalOptionDialog")
                Handler().postDelayed({
                    buttonDoubleDefend = false
                }, 1000)
            }

        }
        // ------ 추가 옵션으로 이동 코드 끝 ------

        // ------ 이전으로 버튼 클릭시 화면 꺼지는 코드 ------
        binding.btnBack.setOnClickListener {
            dismiss()
        }

        // 선택완료 누르면 값을 보내는 코드
        binding.btnComplet.setOnClickListener {

            //클릭하면 onClick 실행 후 값을 보냄

            // ------ 라디오 버튼에서도 값 가져오기 시작------
            val radiogroup = binding.rbCooHot.checkedRadioButtonId
            val radioButton: RadioButton = binding.rbCooHot.findViewById(radiogroup)
            val coolhot: Boolean = radioButton.isChecked


            // CartStorage.productList에 값을 추가
            CartStorage.addProduct(
                Product(
                    name = binding.tvMenuName.text.toString(),
                    price = binding.tvMenuPrice.text.toString().replace(",", "").replace("원", "")
                        .toIntOrNull() ?: 0,
                    count = binding.tvMenuCount.text.toString().toInt(),
                    image = binding.imgMenu.toString()
                )
            )



            dismiss()
        }

        // ------ 라디오 버튼에서도 값 가져오기 시작------

        // ------ 상품의 수량 조절하는 코드 시작 ------
        var MenuCount = 1
        val priceText = binding.tvMenuPrice.text.toString()
        // Plus버튼 누르면 증가하는 코드
        binding.btnSeniorPlus.setOnClickListener {
            MenuCount++
            // 상품 수량이 증가하는 코드
            binding.tvMenuCount.text = MenuCount.toString()

            // 수량에 맞춰 가격이 증가하는 코드
            val MenuPlusCountInt: Int? = binding.tvMenuCount.text.toString().toIntOrNull()
            if (MenuPlusCountInt != null) {
                val modifyprice = priceText
                    .replace(",", "")
                    .replace("원", "")
                    .toIntOrNull() ?: 0
//                val getPrice = arguments?.getInt("sprice") ?: 0
                val plusPrice = modifyprice * MenuPlusCountInt
                binding.tvMenuPrice.text =
                    String.format("%,d원", plusPrice) // String.format("%,d", 값) -> 1000 단위마다 , 표시
            } else {
                binding.tvMenuPrice.text = "취소 후 다시 부탁드립니다."
            }
            binding.btnSeniorMinus.isClickable = true  // Plus버튼 이후에 지속적으로 Minus버튼 클릭시 버튼 활성화
        }

        // Minus버튼 누르면 감소하는 코드
        binding.btnSeniorMinus.setOnClickListener {

            if (MenuCount <= 1) {
                binding.btnSeniorMinus.isClickable = false  // 수량이 1이면 Minus버튼 비활성화
            }

            if (MenuCount > 1) {
                MenuCount--
                // 상품 수랑이 감소하는 코드
                binding.tvMenuCount.text = MenuCount.toString()
                // 수량에 맞춰 가격이 감소하는 코드
                val MenuMinusCountInt: Int? =
                    binding.tvMenuCount.text.toString().toIntOrNull()  //replace
                if (MenuMinusCountInt != null) {
                    val modifyprice = priceText
                        .replace(",", "")
                        .replace("원", "")
                        .toIntOrNull() ?: 0
                    val minusPrice = modifyprice * MenuMinusCountInt
                    binding.tvMenuPrice.text = String.format(
                        "%,d원",
                        minusPrice
                    ) // String.format("%,d", 값) -> 1000 단위마다 , 표시 String.format("%, d 원", minusPrice)
                }

            }

        }
        // ------ 상품의 수량 조절하는 코드 끝 ------

    }
}