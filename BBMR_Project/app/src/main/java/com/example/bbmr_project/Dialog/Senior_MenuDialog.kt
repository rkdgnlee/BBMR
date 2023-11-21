package com.example.bbmr_project.Dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bbmr_project.CartStorage
import com.example.bbmr_project.Product
import com.example.bbmr_project.VO.Senior_TakeOutVO
import com.example.bbmr_project.databinding.DialogSeniorMenuBinding

class Senior_MenuDialog : DialogFragment() {


    var onClick: (Product) -> Unit = {
        val args = Bundle().apply {
            putParcelable(KeyProductBundleKey, it)
        }
        val fragment = Senior_BasketDialog()
        fragment.arguments = args
        fragment.show(childFragmentManager, "")

    }

    // Adapter에서 값을 받아오는 코드
    companion object {
        fun Senior_Menu(item: Senior_TakeOutVO): Senior_MenuDialog {
            val args = Bundle().apply {
                putString("sname", item.sname)
                putInt("sprice", item.sprice)
                putInt("simg", item.simg)
            }
            val fragment = Senior_MenuDialog()
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var binding: DialogSeniorMenuBinding


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

        // 메뉴 선택시 Dialog에 메뉴의 기본정보 제공하는 코드 (Adapter에서 받아온 값을 화면에 보여주기 위한 코드)
        val sname = arguments?.getString("sname")
        val sprice = arguments?.getInt("sprice")
        val simg = arguments?.getInt("simg")

        binding.tvMenuName.text = sname
        binding.tvMenuPrice.text = sprice.toString()
        if (simg != null) {
            binding.imgMenu.setImageResource(simg)
        }

        // ------ 이전으로 버튼 클릭시 화면 꺼지는 코드 ------
        binding.btnBack.setOnClickListener {
            dismiss()
        }

        // 선택완료 누르면 값을 보내는 코드
        binding.btnComplet.setOnClickListener {

            // 버튼 클릭시 어떤 값을 보내는지 확인하는 코드
            val product = Product(
                binding.tvMenuName.text.toString(),
                binding.tvMenuPrice.text.toString().toInt(),
                binding.tvMenuCount.text.toString().toInt()
            )
            Log.d("Senior_MenuDialog", "Selected Product: $product")

            //클릭하면 onClick 실행 후 값을 보냄
            onClick.invoke(
                Product(
                    binding.tvMenuName.text.toString(),
                    binding.tvMenuPrice.text.toString().toInt(),
                    binding.tvMenuCount.text.toString().toInt()
                )
            )

            // CartStorage.productList에 값을 추가
            CartStorage.addProduct(
                Product(
                    binding.tvMenuName.text.toString(),
                    binding.tvMenuPrice.text.toString().toInt(),
                    binding.tvMenuCount.text.toString().toInt()
                )

            )


            dismiss()
        }


        // ------ 상품의 수량 조절하는 코드 시작 ------
        var MenuCount = 1

        // Plus버튼 누르면 증가하는 코드
        binding.btnSeniorPlus.setOnClickListener {
            MenuCount++
            // 상품 수량이 증가하는 코드
            binding.tvMenuCount.text = MenuCount.toString()
            // 수량에 맞춰 가격이 증가하는 코드
            val MenuPlusCountInt : Int? = binding.tvMenuCount.text.toString().toIntOrNull()
            if (MenuPlusCountInt != null){
                val getPrice = arguments?.getInt("sprice") ?:0
                val plusPrice = getPrice*MenuPlusCountInt
                binding.tvMenuPrice.text = plusPrice.toString()
            }else{
                binding.tvMenuPrice.text = "취소 후 다시 부탁드립니다."
            }
            binding.btnSeniorMinus.isClickable = true  // Plus버튼 이후에 지속적으로 Minus버튼 클릭시 버튼 활성화
        }

        // Minus버튼 누르면 감소하는 코드
        binding.btnSeniorMinus.setOnClickListener {

            if (MenuCount == 1) {
                binding.btnSeniorMinus.isClickable = false  // 수량이 1이면 Minus버튼 비활성화
            }

            if (MenuCount > 1) {
                MenuCount--
                // 상품 수랑이 감소하는 코드
                binding.tvMenuCount.text = MenuCount.toString()
                // 수량에 맞춰 가격이 감소하는 코드
                val MenuMinusCountInt : Int? = binding.tvMenuCount.text.toString().toIntOrNull()
                if (MenuMinusCountInt != null){
                    val getPrice = arguments?.getInt("sprice") ?: 0
                    val minusPrice = getPrice*MenuMinusCountInt
                    binding.tvMenuPrice.text = minusPrice.toString()
                }

            }

        }
        // ------ 상품의 수량 조절하는 코드 끝 ------


    }
}