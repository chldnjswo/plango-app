package com.plango.app.data.travel

import com.google.gson.annotations.SerializedName

data class TravelDetailResponse(
    @SerializedName("travelId") val travelId: Long,
    @SerializedName("userPublicId") val userPublicId: String,
    @SerializedName("travelType") val travelType: String,
    @SerializedName("travelDest") val travelDest: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("themes") val themes: List<String>,
    @SerializedName("companionType") val companionType: String,
    @SerializedName("days") val days: List<Day>?,
    @SerializedName("createdAt") val createdAt: String
) : java.io.Serializable {

    data class Day(
        @SerializedName("dayIndex") val dayIndex: Int,
        @SerializedName("courses") val courses: List<Course>
    ) : java.io.Serializable

    data class Course(
        @SerializedName("order") val order: Int,
        @SerializedName("locationName") val locationName: String,
        @SerializedName("lat") val lat: Double?,
        @SerializedName("lng") val lng: Double?,
        @SerializedName("note") val note: String?,
        @SerializedName("theme") val theme: String?,
        @SerializedName("howLong") val howLong: Int?
    ) : java.io.Serializable
}
