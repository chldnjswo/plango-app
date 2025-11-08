package com.plango.app.data.travel

import com.google.gson.annotations.SerializedName
import com.plango.app.ui.generate.CompanionItem
import java.util.Date

data class TravelReadResponse(
    @SerializedName("travelId") val travelId: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("destination") val destination: String,
    @SerializedName("startDate") val startDate: Date,
    @SerializedName("endDate") val endDate: Date,
    @SerializedName("companionType") val companionType: CompanionItem.CompanionType,
    @SerializedName("theme1") val theme1: String,
    @SerializedName("theme2") val theme2: String,
    @SerializedName("theme3") val theme3: String
)