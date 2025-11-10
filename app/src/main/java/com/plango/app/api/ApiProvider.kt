package com.plango.app.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiProvider {
    private const val BASE_URL = "http://54.214.189.128:8080"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)   // 연결 시도 제한
        .readTimeout(60, TimeUnit.SECONDS)      // 서버 응답 읽기 제한
        .writeTimeout(60, TimeUnit.SECONDS)     // 서버로 데이터 전송 제한
        .build()

    val api : ApiService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}