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
import kotlinx.coroutines.launch
import kotlin.jvm.java

class PageLoading : AppCompatActivity() {
    private lateinit var binding: ActivityPageLoadingBinding
    private val userViewModel : UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPageLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName")
        binding.tvLoadingText.text = "${userName}님의 정보를 외우고 있어요"

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userResponseFlow.collect { response ->
                    if (response != null) {
                        val intent = Intent(this@PageLoading, HomeActivity::class.java)
                        Toast.makeText(this@PageLoading, "로그인 성공! 유저 키: ${response.publicId}", Toast.LENGTH_SHORT).show()

                        intent.putExtra("userName", userName)

                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }




}