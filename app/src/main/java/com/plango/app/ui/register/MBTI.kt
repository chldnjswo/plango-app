package com.plango.app.ui.register

import android.R
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.plango_nickname.databinding.ActivityMbtiBinding
import com.example.plango_nickname.user.UserViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.forEach
import kotlin.jvm.java

class MBTI : AppCompatActivity() {

    private lateinit var binding: ActivityMbtiBinding
    private val userViewModel: UserViewModel by viewModels()
    private var selectedMbti: String? = null
    private var userName: String = ""

    private val mbtiButtons by lazy {
        listOf(
            binding.btnISTJ, binding.btnISFJ, binding.btnINFJ, binding.btnINTJ,
            binding.btnISTP, binding.btnISFP, binding.btnINFP, binding.btnINTP,
            binding.btnESTP, binding.btnESFP, binding.btnENFP, binding.btnENTP,
            binding.btnESTJ, binding.btnESFJ, binding.btnENFJ, binding.btnENTJ
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMbtiBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userName = intent.getStringExtra("userName") ?: ""
        binding.gridMbti.visibility=View.INVISIBLE
        binding.nextButton.visibility=View.INVISIBLE


        lifecycleScope.launch {
            typeTextEffect(binding.tvQuestion, "\"${userName}님에 대해서 궁금해요 \uD83E\uDD14\"", 55)
            delay(800)
            typeTextEffect(binding.tvExplain, "MBTI가 어떻게 되시나요?", 55)



            //  모든 문장 출력 후 입력창 & 버튼 나타나기
            delay(600)
            showWithFade(binding.gridMbti)
            delay(200)
            showWithFade(binding.nextButton)
        }



        mbtiButtons.forEach { button ->
            button.setOnClickListener {
                selectedMbti = button.text.toString()
                updateSelectedButton(button)
            }
        }

        //  다음 버튼 클릭 시
        binding.nextButton.setOnClickListener {
            val mbti = selectedMbti
            if (mbti == null) {
                Toast.makeText(this, "MBTI를 선택해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@MBTI, PageLoading::class.java) //PageLoading으로 바꿔줘야함
                intent.putExtra("userName", userName)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                userViewModel.createUser(userName, mbti)
            }
        }

    }



    private fun updateSelectedButton(selectedButton: MaterialButton) {
        // 색상 정의
        val colorSelectedBg = ContextCompat.getColor(this, R.color.button_primary) // #E07559
        val colorUnselectedBg = ContextCompat.getColor(this, R.color.card_background)
        val colorStroke = ContextCompat.getColor(this, R.color.card_border)
        val colorSelectedText = ContextCompat.getColor(this, R.color.white) // 흰색
        val colorUnselectedText = ContextCompat.getColor(this, R.color.text_primary)

        mbtiButtons.forEach { btn ->
            if (btn == selectedButton) {

                btn.backgroundTintList = ColorStateList.valueOf(colorSelectedBg)
                btn.strokeWidth = 0
                btn.setTextColor(colorSelectedText)

                //커지게 하는 애니메이션
                btn.animate()
                    .scaleX(1.08f)
                    .scaleY(1.08f)
                    .setDuration(120)
                    .start()
            } else {
                //  선택 해제된 버튼 스타일
                btn.backgroundTintList = ColorStateList.valueOf(colorUnselectedBg)
                btn.strokeColor = ColorStateList.valueOf(colorStroke)
                btn.strokeWidth = 2
                btn.setTextColor(colorUnselectedText)

                // 원래 크기로 복귀
                btn.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(120)
                    .start()
            }
        }
    }
    private suspend fun typeTextEffect(textView: TextView, text: String, delayMs: Long = 60L) {
        textView.text = ""
        for (char in text) {
            textView.append(char.toString())
            delay(delayMs)
        }
    }

    //  부드럽게 나타나는 페이드인 효과
    private fun showWithFade(view: View, duration: Long = 500L) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .start()
    }

}