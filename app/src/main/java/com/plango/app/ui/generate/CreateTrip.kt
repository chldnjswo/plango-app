package com.example.plango_nickname

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.plango_nickname.databinding.ActivityCreateTripBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateTrip : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTripBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDomestic.visibility=View.INVISIBLE
        binding.btnOverseas.visibility= View.INVISIBLE

        lifecycleScope.launch {
            typeTextEffect(binding.tvQuestion, "여행 계획을 \n만들어 볼까요?", 55)
            delay(800)
            typeTextEffect(binding.tvIntro, "국내? OR 해외?", 55)

            delay(600)
            showWithFade(binding.btnDomestic)
            delay(200)
            showWithFade(binding.btnOverseas)

        }
        binding.btnDomestic.setOnClickListener {
            var userName = intent.getStringExtra("userName")

            var intent = android.content.Intent(this, DomesticTrip::class.java)
            intent.putExtra("userName", userName)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        binding.btnOverseas.setOnClickListener {
            var userName = intent.getStringExtra("userName")

            var intent = android.content.Intent(this, OverseasTrip::class.java)
            intent.putExtra("userName", userName)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        }





    }
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