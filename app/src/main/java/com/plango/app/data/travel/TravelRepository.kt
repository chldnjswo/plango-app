package com.plango.app.data.travel

import com.plango.app.api.ApiProvider
import com.plango.app.api.ApiService
import com.plango.app.ui.generate.CompanionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

object TravelRepository {
    private val api: ApiService = ApiProvider.api

    // 여행 정보 캐시용 StateFlow
    private val _travelFlow = MutableStateFlow<TravelReadResponse?>(null)
    val travelFlow: StateFlow<TravelReadResponse?> = _travelFlow

    suspend fun createTravelAndCache(
        destination: String,
        startDate: Date,
        endDate: Date,
        companionType: CompanionItem.CompanionType,
        theme1: String,
        theme2: String,
        theme3: String
    ) {

    }
}