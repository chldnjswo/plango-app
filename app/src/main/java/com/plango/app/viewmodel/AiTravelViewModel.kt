/*package com.plango.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plango.app.data.ai.AiRepository
import com.plango.app.data.ai.AiTravelRequest
import com.plango.app.data.ai.AiTravelResponse
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AiTravelViewModel : ViewModel() {
    private val repository = AiRepository
    val aiTravelFlow: StateFlow<AiTravelResponse?> = repository.aiTravelFlow

    fun requestAiPlan(
        travelType: String,
        travelDest: String,
        startDate: String,
        endDate: String,
        theme1: String,
        theme2: String,
        theme3: String,
        userMbti: String,
        companionType: String
    ) {
        val req = AiTravelRequest(
            travelType, travelDest, startDate, endDate,
            theme1, theme2, theme3, userMbti, companionType
        )
        viewModelScope.launch {
            repository.requestAiTravelPlan(req)
        }
    }
}
*/