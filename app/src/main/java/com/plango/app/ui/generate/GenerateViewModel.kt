package com.plango.app.ui.generate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GenerateViewModel : ViewModel() {
    enum class CompanionType { ALONE, COUPLE, FAMILY, FRIENDS }

    // 여행지
    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> get() = _destination

    // 시작일
    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> get() = _startDate

    // 종료일
    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> get() = _endDate

    // 동행자 타입
    private val _companionType = MutableLiveData<CompanionType?>()
    val companionType: LiveData<CompanionType?> get() = _companionType

    // 여행 테마 (3개 저장)
    private val _themes = MutableLiveData<List<String>>()
    val themes: LiveData<List<String>> get() = _themes

    fun setDestination(value: String) {
        _destination.value = value
    }

    fun setStartDate(value: String) {
        _startDate.value = value
    }

    fun setEndDate(value: String) {
        _endDate.value = value
    }

    fun setCompanionType(value: CompanionType) {
        _companionType.value = value
    }

    fun setThemes(values: List<String>) {
        _themes.value = values
    }

    // 서버 전송용 함수
    fun toRequestMap(): Map<String, Any>? {

        val dest = _destination.value
        val start = _startDate.value
        val end = _endDate.value
        val comp = _companionType.value
        val themeList = _themes.value

        // 예외처리
        if (dest.isNullOrEmpty() || start.isNullOrEmpty() ||
            end.isNullOrEmpty() || comp == null ||
            themeList == null || themeList.size != 3
        ) return null

        return mapOf(
            "destination" to dest,
            "start_date" to start,
            "end_date" to end,
            "companion_type" to comp.toString(),
            "themes" to themeList
        )
    }
}