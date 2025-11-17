package com.plango.app.ui.generate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GenerateViewModel : ViewModel() {
    enum class CompanionType { SOLO, COUPLE, FAMILY, FRIEND }

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

    private val _travelType = MutableLiveData<String>()
    val travelType: LiveData<String> get() = _travelType

    fun setTravelType(value: String) {
        _travelType.value = value
    }

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
}