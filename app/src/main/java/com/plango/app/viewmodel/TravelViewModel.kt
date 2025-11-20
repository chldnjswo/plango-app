package com.plango.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plango.app.data.travel.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TravelViewModel : ViewModel() {

    private val repository = TravelRepository

    // 여행 상세
    val travelDetailFlow: StateFlow<TravelDetailResponse?> = repository.travelDetailFlow

    // ⭐ 리스트 3종류 각각 가져오기
    val ongoingFlow: StateFlow<List<TravelSummaryResponse>> = repository.ongoingFlow
    val upcomingFlow: StateFlow<List<TravelSummaryResponse>> = repository.upcomingFlow
    val finishedFlow: StateFlow<List<TravelSummaryResponse>> = repository.finishedFlow


    /** 여행 생성 */
    fun createTravel(
        userPublicId: String,
        travelType: String,
        travelDest: String,
        startDate: String,
        endDate: String,
        themes: List<String>,
        companionType: String
    ) = viewModelScope.launch {
        repository.clearTravelDetail()
        val request = TravelCreateRequest(
            userPublicId = userPublicId,
            travelType = travelType,
            travelDest = travelDest,
            startDate = startDate,
            endDate = endDate,
            themes = themes,
            companionType = companionType
        )
        repository.createTravel(request)
    }

    /** 진행 중 여행 불러오기 */
    fun loadOngoing(publicId: String) = viewModelScope.launch {
        repository.getOngoingTravels(publicId)
    }

    /** 다가올 여행 불러오기 */
    fun loadUpcoming(publicId: String) = viewModelScope.launch {
        repository.getUpcomingTravels(publicId)
    }

    /** 지난 여행 불러오기 */
    fun loadFinished(publicId: String) = viewModelScope.launch {
        repository.getFinishedTravels(publicId)
    }

    /** 상세 조회 */
    fun getTravelDetail(travelId: Long) = viewModelScope.launch {
        repository.getTravelDetail(travelId)
    }
}
