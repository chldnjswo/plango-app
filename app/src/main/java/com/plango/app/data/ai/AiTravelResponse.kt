package com.plango.app.data.ai

import com.google.gson.annotations.SerializedName

data class AiTravelResponse(
    @SerializedName("days")
    val days: List<AiDay>
) {
    data class AiDay(
        @SerializedName("courses")
        val courses: List<AiCourse>
    )

    data class AiCourse(
        @SerializedName("order")
        val order: Int,

        @SerializedName("locationName")
        val locationName: String,

        @SerializedName("lat")
        val lat: Double?,

        @SerializedName("lng")
        val lng: Double?,

        @SerializedName("note")
        val note: String?,

        @SerializedName("theme")
        val theme: String,

        @SerializedName("howLong")
        val howLong: Int
    )
}
