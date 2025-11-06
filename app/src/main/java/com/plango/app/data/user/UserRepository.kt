package com.plango.app.data.user

import android.util.Log
import com.example.plango_nickname.api.ApiProvider
import com.example.plango_nickname.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.log

object UserRepository {
    private val api: ApiService = ApiProvider.api

    // 유저 정보 캐시용 StateFlow
    private val _userFlow = MutableStateFlow<UserReadResponse?>(null)
    val userFlow: StateFlow<UserReadResponse?> = _userFlow





    suspend fun createUserAndCache(name: String, mbti: String) {
        try {

            val request = UserRequest(name, mbti)
            val result = api.createUser(request)
            val publicId = result.publicId
            Log.d("UserRepository", " 서버로부터 publicId 받음: $publicId")


            val response = api.getUser(publicId)
            Log.d("UserRepository", " 서버 응답: $response")

            _userFlow.value = response

        } catch (e: Exception) {
            Log.e("UserRepository", "오류 발생: ${e.message}", e)
        }
    }
}
