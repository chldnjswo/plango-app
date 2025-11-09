package com.plango.app.data.travel

import android.util.Log
import com.plango.app.api.ApiProvider
import com.plango.app.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object TravelRepository {
    private val api: ApiService = ApiProvider.api

    // 여행 생성 후 상세정보 캐시
    private val _travelDetailFlow = MutableStateFlow<TravelDetailResponse?>(null)
    val travelDetailFlow: StateFlow<TravelDetailResponse?> = _travelDetailFlow

    // 목록 캐시
    private val _travelListFlow = MutableStateFlow<List<TravelSummaryResponse>>(emptyList())
    val travelListFlow: StateFlow<List<TravelSummaryResponse>> = _travelListFlow


    // 여행 생성
    suspend fun createTravel(request: TravelCreateRequest) {
        try {
            val response = api.createTravel(request)
            Log.d("TravelRepository", "여행 생성 성공: $response")
            _travelDetailFlow.value = response
        } catch (e: Exception) {
            Log.e("TravelRepository", "여행 생성 실패: ${e.message}", e)
        }
    }


    //다가 올 여행
    suspend fun getUpcomingTravels(userPublicId: String) {
        try {
            val response = api.getUpcomingTravels(userPublicId)
            Log.d("TravelRepository", "다가올 여행 ${response.size}건 수신")
            _travelListFlow.value = response
        } catch (e: Exception) {
            Log.e("TravelRepository", "다가올 여행 불러오기 실패: ${e.message}", e)
        }
    }

    //지난여행
    suspend fun getFinishedTravels(userPublicId: String) {
        try {
            val response = api.getFinishedTravels(userPublicId)
            Log.d("TravelRepository", "지난 여행 ${response.size}건 수신")
            _travelListFlow.value = response
        } catch (e: Exception) {
            Log.e("TravelRepository", "지난 여행 불러오기 실패: ${e.message}", e)
        }
    }

    //진행중인 여행
    suspend fun getOngoingTravels(userPublicId: String) {
        try {
            val response = api.getOngoingTravels(userPublicId)
            Log.d("TravelRepository", "진행 중 여행 ${response.size}건 수신")
            _travelListFlow.value = response
        } catch (e: Exception) {
            Log.e("TravelRepository", "진행 중 여행 불러오기 실패: ${e.message}", e)
        }
    }

    //  특정 여행 상세정보 불러오기
    suspend fun getTravelDetail(travelId: Long) {
        try {
            val response = api.getTravelDetail(travelId)
            Log.d("TravelRepository", "여행 상세 조회 성공: $response")
            _travelDetailFlow.value = response
        } catch (e: Exception) {
            Log.e("TravelRepository", "여행 상세 조회 실패: ${e.message}", e)
        }
    }
}
