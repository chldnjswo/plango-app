package com.plango.app.data.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private const val DS_NAME = "user_prefs" // "user_prefs" 라는 이름의 데이터 스토어 파일명 지정
val Context.dataStore by preferencesDataStore(name = DS_NAME) // 데이터 스토어 객체 실제 생성 지점. 싱글톤으로 생성

/*
    실제 파일 경로는 패키지명/files/datastore/user_prefs.preferences_pb
    val userFlow = context.dataStore.data 로 어느 액티비티/프래그먼트에서든 접근 가능
    
    DataStore는 항상 key-value 방식
*/

object UserPrefs {
    private val KEY_USER_ID = stringPreferencesKey("user_id")
    // 저장할 키 정의. "user_id" 라는 필드 명에 문자열 타입을 저장

    suspend fun getUserIdOnce(ctx: Context): String? =
        ctx.dataStore.data.map { it[KEY_USER_ID] }.firstOrNull()
    /*
        유저 키 읽기 함수. 앱 시작 시 한 번만 검사하면 됨.

         ctx.dataStore.data -> 데이터 스토어 불러오기
        .map { it[KEY_USER_ID] } -> user_id 값으로 맵에 접근
        .firstOrNull() -> 첫 번째 데이터만 가져옴
    */

    suspend fun saveUserId(ctx: Context, id: String) {
        ctx.dataStore.edit { it[KEY_USER_ID] = id }
    }
    /*
        유저 키 저장 함수.

        edit { } 블록 내에서 데이터스토어 내용 수정 가능
        it[KEY_USER_ID] = id -> "user_id"라는 이름으로 id를 저장

        유저 키 저장법
        UserPrefs.saveUserId(context, "서버 UUID 값")
    */

    suspend fun clearUserId(ctx: Context) {
        ctx.dataStore.edit { it.remove(KEY_USER_ID) }
    }
    /*
        유저 키 삭제 함수.
    */
}