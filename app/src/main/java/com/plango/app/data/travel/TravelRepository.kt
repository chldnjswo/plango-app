package com.plango.app.data.travel

import android.util.Log
import com.plango.app.api.ApiProvider
import com.plango.app.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException

object TravelRepository {

    private val api: ApiService = ApiProvider.api

    // 여행 생성 후 상세정보 캐시
    private val _travelDetailFlow = MutableStateFlow<TravelDetailResponse?>(null)
    val travelDetailFlow: StateFlow<TravelDetailResponse?> = _travelDetailFlow


    // ⭐ 리스트 3종류를 각각 분리!
    private val _ongoingFlow = MutableStateFlow<List<TravelSummaryResponse>>(emptyList())
    val ongoingFlow: StateFlow<List<TravelSummaryResponse>> = _ongoingFlow

    private val _upcomingFlow = MutableStateFlow<List<TravelSummaryResponse>>(emptyList())
    val upcomingFlow: StateFlow<List<TravelSummaryResponse>> = _upcomingFlow

    private val _finishedFlow = MutableStateFlow<List<TravelSummaryResponse>>(emptyList())
    val finishedFlow: StateFlow<List<TravelSummaryResponse>> = _finishedFlow


    // ⭐ 여행 생성
    suspend fun createTravel(request: TravelCreateRequest) {
        try {
            val response = api.createTravel(request)
            Log.d("TravelRepository", "여행 생성 성공: $response")
            _travelDetailFlow.value = response

        } catch (e: HttpException) {

            // 🔥 핵심: 서버가 내려준 에러 메시지 추출
            val errorBody = e.response()?.errorBody()?.string()

            Log.e(
                "TravelRepository",
                """
                ❌ 여행 생성 실패 (HttpException)
                code: ${e.code()}
                errorBody: $errorBody
                """.trimIndent()
            )

        } catch (e: Exception) {

            Log.e("TravelRepository", "여행 생성 실패(기타 Exception): ${e.message}", e)
        }
    }

    // ⭐ 진행 중인 여행
    suspend fun getOngoingTravels(publicId: String) {
        try {
            val response = api.getOngoingTravels(publicId)
            Log.d("TravelRepository", "진행중 여행 ${response.size}건 수신")
            _ongoingFlow.value = response
        } catch (e: Exception) {
            Log.e("TravelRepository", "진행중 여행 실패: ${e.message}", e)
            _ongoingFlow.value = emptyList()
        }
    }

    // ⭐ 다가올 여행
    suspend fun getUpcomingTravels(publicId: String) {
        try {
            val response = api.getUpcomingTravels(publicId)
            Log.d("TravelRepository", "다가올 여행 ${response.size}건 수신")
            _upcomingFlow.value = response
        } catch (e: Exception) {
            Log.e("TravelRepository", "다가올 여행 실패: ${e.message}", e)
            _upcomingFlow.value = emptyList()
        }
    }

    // ⭐ 지난 여행
    suspend fun getFinishedTravels(publicId: String) {
        try {
            val response = api.getFinishedTravels(publicId)
            Log.d("TravelRepository", "지난 여행 ${response.size}건 수신")
            _finishedFlow.value = response
        } catch (e: Exception) {
            Log.e("TravelRepository", "지난 여행 실패: ${e.message}", e)
            _finishedFlow.value = emptyList()
        }
    }

    // ⭐ 특정 여행 상세조회
    suspend fun getTravelDetail(travelId: Long) {
        try {
            val response = api.getTravelDetail(travelId)
            Log.d("TravelRepository", "여행 상세 조회 성공: $response")
            _travelDetailFlow.value = response
        } catch (e: Exception) {
            Log.e("TravelRepository", "여행 상세 조회 실패: ${e.message}", e)
        }
    }
    fun clearTravelDetail() {
        _travelDetailFlow.value = null
    }
}
