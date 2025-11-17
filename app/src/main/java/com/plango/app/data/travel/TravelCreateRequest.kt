package com.plango.app.data.travel

import com.google.gson.annotations.SerializedName

data class TravelCreateRequest(
    @SerializedName("userPublicId")
    val userPublicId: String,

    @SerializedName("travelType")
    val travelType: String,

    @SerializedName("travelDest")
    val travelDest: String,

    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("themes")
    val themes: List<String>,

    @SerializedName("companionType")
    val companionType: String
)
