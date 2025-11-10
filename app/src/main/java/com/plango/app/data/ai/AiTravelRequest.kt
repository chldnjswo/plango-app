package com.plango.app.data.ai

import com.google.gson.annotations.SerializedName

data class AiTravelRequest(
    @SerializedName("travelType")
    val travelType: String, // "DOMESTIC" or "OVERSEAS"

    @SerializedName("travelDest")
    val travelDest: String, // ex: "부산"

    @SerializedName("startDate")
    val startDate: String, // "2025-12-24"

    @SerializedName("endDate")
    val endDate: String, // "2025-12-28"

    @SerializedName("theme1")
    val theme1: String,

    @SerializedName("theme2")
    val theme2: String,

    @SerializedName("theme3")
    val theme3: String,

    @SerializedName("userMbti")
    val userMbti: String, // ex: "INFP"

    @SerializedName("companionType")
    val companionType: String // ex: "SOLO", "COUPLE", "FAMILY", "FRIEND"
)
