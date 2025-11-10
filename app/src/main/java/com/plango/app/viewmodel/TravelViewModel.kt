package com.plango.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plango.app.data.travel.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TravelViewModel : ViewModel() {

    private val repository = TravelRepository

    val travelDetailFlow: StateFlow<TravelDetailResponse?> = repository.travelDetailFlow
    val travelListFlow: StateFlow<List<TravelSummaryResponse>> = repository.travelListFlow


    /**  여행 생성 */
    fun createTravel(
        userPublicId: String,
        travelType: String,
        travelDest: String,
        startDate: String,
        endDate: String,
        themes: List<String>,
        companionType: String
    ) {
        viewModelScope.launch {
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
    }

    /**  목록 불러오기 */
    fun getUpcomingTravels(publicId: String) = viewModelScope.launch {
        repository.getUpcomingTravels(publicId)
    }

    fun getFinishedTravels(publicId: String) = viewModelScope.launch {
        repository.getFinishedTravels(publicId)
    }

    fun getOngoingTravels(publicId: String) = viewModelScope.launch {
        repository.getOngoingTravels(publicId)
    }

    /**  상세조회 */
    fun getTravelDetail(travelId: Long) = viewModelScope.launch {
        repository.getTravelDetail(travelId)
    }
}
