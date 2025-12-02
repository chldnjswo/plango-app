package com.plango.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plango.app.data.user.UserReadResponse
import com.plango.app.data.user.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserRepository


    val userResponseFlow: StateFlow<UserReadResponse?> = repository.userFlow

    fun getUserById(publicId: String) {
        viewModelScope.launch {
            repository.getUserById(publicId)
        }
    }
    fun createUser(name: String, mbti: String) {
        viewModelScope.launch {
            repository.createUserAndCache(name, mbti)
        }
    }
    fun updateUser(publicId: String, nickname: String, mbti: String) {
        viewModelScope.launch {
            val ok = repository.updateUser(publicId, nickname, mbti)
            if (ok) {
                // 업데이트 후 최신 프로필 다시 가져오기
                repository.getUserById(publicId)
            }
        }
    }
}
