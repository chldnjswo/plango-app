/*package com.plango.app.data.ai

import android.util.Log
import com.plango.app.api.ApiProvider
import com.plango.app.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AiRepository {
    private val api : ApiService = ApiProvider.api

    private val _aiTravelFlow = MutableStateFlow<AiTravelResponse?>(null)
    val aiTravelFlow: StateFlow<AiTravelResponse?> = _aiTravelFlow

    suspend fun requestAiTravelPlan(request: AiTravelRequest) {
        try {
            val response = api.getAiRecommendedTravel(request)
            Log.d("AiRepository", "✅ AI 응답 수신 완료: ${response.days.size}일 일정")
            _aiTravelFlow.value = response
        } catch (e: Exception) {
            Log.e("AiRepository", "❌ AI 여행 요청 실패: ${e.message}", e)
        }
    }
}
*/