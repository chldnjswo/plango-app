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
}
