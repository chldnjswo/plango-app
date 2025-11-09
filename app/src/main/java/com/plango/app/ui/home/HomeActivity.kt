package com.plango.app.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.plango.app.R
import com.plango.app.data.user.UserPrefs
import com.plango.app.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userNameFromIntent = intent.getStringExtra("userName")

        lifecycleScope.launch {
            if (userNameFromIntent == null) {
                //  앱 재시작 시엔 DataStore에서 userId 읽어서 서버로부터 유저 이름 가져오기
                val userId = UserPrefs.getUserIdOnce(this@HomeActivity)
                if (!userId.isNullOrEmpty()) {
                    userViewModel.getUserById(userId)
                }

                // Flow 수집 (LiveData가 아니라 collectLatest!)
                userViewModel.userResponseFlow.collectLatest { user ->
                    if (user != null) {
                        Log.d("HomeActivity", "서버에서 가져온 userName=${user.name}")
                        showHomeFragment(user.name)
                    }
                }
            } else {
                showHomeFragment(userNameFromIntent)
            }
        }
    }

    private fun showHomeFragment(userName: String) {
        val fragment = HomeStep1().apply {
            arguments = Bundle().apply {
                putString("userName", userName)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
