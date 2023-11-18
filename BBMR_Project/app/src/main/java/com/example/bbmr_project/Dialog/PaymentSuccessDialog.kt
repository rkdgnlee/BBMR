package com.example.bbmr_project.Dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bbmr_project.Normal_MainActivity
import com.example.bbmr_project.databinding.DialogPaymentSuccessBinding

class PaymentSuccessDialog: DialogFragment() {
    private lateinit var  binding: DialogPaymentSuccessBinding

    override fun onStart() {
        super.onStart()
        val darkTransparentBlack = Color.argb((255 * 0.6).toInt(), 0, 0, 0)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(darkTransparentBlack))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setDimAmount(0.4f)
        dialog?.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPaymentSuccessBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 텍스트 뷰 텍스트 크기 조정
        val tvPaySuccess = binding.tvPaySuccess
        val _text = "주문 성공!\n주문번호를 받아가세요"
        val spannableStringBuilder = SpannableStringBuilder(_text)
        spannableStringBuilder.setSpan(RelativeSizeSpan(2f), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringBuilder.setSpan(RelativeSizeSpan(0.9f),7, _text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvPaySuccess.text = spannableStringBuilder
        //




        val Handler = Handler(Looper.getMainLooper())
        Handler.postDelayed({
            val intent = Intent(view.context, Normal_MainActivity::class.java)
            startActivity(intent)
        }, 3500)


    }
}
