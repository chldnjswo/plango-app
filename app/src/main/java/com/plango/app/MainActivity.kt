package com.plango.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.plango.app.databinding.ActivityMainBinding
import com.plango.app.ui.register.RegisterActivity
import androidx.lifecycle.lifecycleScope
import com.plango.app.data.user.UserPrefs
import com.plango.app.ui.generate.GenerateActivity
import com.plango.app.ui.home.HomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // launch 블록 전체 코루틴으로 실행 -> 액티비티가 죽으면 코루틴도 취소
        lifecycleScope.launch {

            /*
                !!! Context를 this@MainActivity로 어노테이션을 사용하는 이유 !!!
                    코루틴 내에서 this는 MainActivity가 아닌 코루틴을 가리킴
                    정확히 MainActivity 타입의 this를 지정하기 위해 어노테이션 사용
            */

            // 데이터 스토어에서 키 값 읽어옴
            val userId = UserPrefs.getUserIdOnce(this@MainActivity)

            // 조건문 순차 실행
            if (userId.isNullOrEmpty()) {
                // 유저키 없음 -> 등록 페이지
                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
            }
            else {
                // 유저키 있음 -> 홈
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            }
            finish() // MainActivity는 라우팅 전용 화면이기 때문에 라우팅 후 백그라운드에서 종료
        }
    }
}