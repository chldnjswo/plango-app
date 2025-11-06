package com.example.plango_nickname

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.plango_nickname.databinding.ActivityMainBinding
import com.example.plango_nickname.user.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterFixActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.nicknameInput.visibility = View.INVISIBLE
        binding.nextButton.visibility = View.INVISIBLE


        //  순차 타이핑 효과 실행
        lifecycleScope.launch {
            typeTextEffect(binding.tvGreeting, "안녕하세요 👋\n만나서 반가워요.", 55)
            delay(800)
            typeTextEffect(binding.tvIntro, "저는 떠날지도의 AI 여행비서예요!", 55)
            delay(800)
            typeTextEffect(binding.tvExplain, "사용자님에 대해서 몇 가지\n알고 싶어요! 📝", 55)
            delay(800)
            typeTextEffect(binding.tvQuestion, "성함이 어떻게 되시나요?", 55)

            //  모든 문장 출력 후 입력창 & 버튼 나타나기
            delay(600)
            showWithFade(binding.nicknameInput)
            delay(200)
            showWithFade(binding.nextButton)
        }

        //  다음 버튼 클릭 시
        binding.nextButton.setOnClickListener {
            val name = binding.nicknameInput.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "사용하실 이름을 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MBTI::class.java)
                intent.putExtra("userName", name)
                startActivity(intent)

                //  슬라이드 전환 애니메이션
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }

    //  글자 하나씩 표시하는 함수
    private suspend fun typeTextEffect(textView: android.widget.TextView, text: String, delayMs: Long = 60L) {
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
