package com.plango.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class RegisterViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // ✅ LiveData 버전 (Fragment에서 observe 가능)
    val nickname: LiveData<String> = savedStateHandle.getLiveData("nickname", "")
    val mbti: LiveData<String> = savedStateHandle.getLiveData("mbti", "")

    // ✅ 값 변경 함수
    fun setNickname(value: String) {
        savedStateHandle["nickname"] = value
    }

    fun setMbti(value: String) {
        savedStateHandle["mbti"] = value
    }
}
