package com.plango.app.util

import android.view.View
import android.widget.TextView
import kotlinx.coroutines.delay

object UiEffect {

    // ✨ 텍스트 타이핑 효과
    suspend fun typeTextEffect(textView: TextView, text: String, delayMs: Long = 60L) {
        textView.text = ""
        for (char in text) {
            textView.append(char.toString())
            delay(delayMs)
        }
    }

    // ✨ 부드럽게 나타나는 페이드인 효과
    fun showWithFade(view: View, duration: Long = 500L) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .start()
    }
}
