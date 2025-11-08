package com.plango.app.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    // 닉네임 저장
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get() = _nickname

    // MBTI 저장 (다음 단계에서 사용 예정)
    private val _mbti = MutableLiveData<String>() //다른 프래그먼트에서도 관촬 가능
    val mbti: LiveData<String> get() = _mbti

    fun setNickname(name: String) {
        _nickname.value = name
    }

    fun setMbti(type: String) {
        _mbti.value = type
    }
}