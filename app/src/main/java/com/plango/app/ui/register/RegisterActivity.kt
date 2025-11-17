package com.plango.app.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.plango.app.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 처음 화면을 RegisterStep1Fragment로 설정
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegisterStep1())
                .commit()
        }
    }

    // 다음 단계로 이동할 때 호출할 함수
    fun moveToNextFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
