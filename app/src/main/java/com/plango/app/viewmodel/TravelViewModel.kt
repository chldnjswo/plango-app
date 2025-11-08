package com.plango.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plango.app.data.travel.TravelReadResponse
import com.plango.app.data.travel.TravelRepository
import com.plango.app.data.user.UserReadResponse
import com.plango.app.ui.generate.CompanionItem
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class TravelViewModel : ViewModel() {
    private val repository = TravelRepository
    val userResponseFlow: StateFlow<TravelReadResponse?> = repository.travelFlow

    fun createTravel(
        destination: String,
        startDate: Date,
        endDate: Date,
        companionType: CompanionItem.CompanionType,
        theme1: String,
        theme2: String,
        theme3: String
    ) {
        viewModelScope.launch {
            repository.createTravelAndCache(
                destination, startDate, endDate, companionType, theme1, theme2, theme3
            )
        }
    }
}
