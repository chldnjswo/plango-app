package com.plango.app.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.plango.app.databinding.ActivityPageLoadingBinding
import com.plango.app.ui.main.HomeActivity
import com.plango.app.viewmodel.UserViewModel
import com.plango.app.data.user.UserPrefs
import kotlinx.coroutines.launch
import kotlin.jvm.java

class PageLoading : AppCompatActivity() {
    private lateinit var binding: ActivityPageLoadingBinding
    private val userViewModel : UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPageLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name") ?: ""
        val mbti = intent.getStringExtra("mbti") ?: ""
        binding.tvLoadingText.text = "${name}님의 정보를 외우고 있어요"

        // 예외처리
        if (name.isBlank() || mbti.isBlank()) {
            Toast.makeText(this, "이름/MBTI가 누락되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // DB 저장 로직
        userViewModel.createUser(name, mbti)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userResponseFlow.collect { response ->
                    if (response != null) {
                        Toast.makeText(this@PageLoading, "로그인 성공! 유저 키: ${response.publicId}", Toast.LENGTH_SHORT).show()
                        UserPrefs.saveUserId(this@PageLoading, response.publicId) // 유저 키 값 저장

                        val intent = Intent(this@PageLoading, HomeActivity::class.java)

                        intent.putExtra("userName", name)

                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }




}