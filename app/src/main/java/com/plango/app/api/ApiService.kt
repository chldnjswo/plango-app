package com.plango.app.api


import com.plango.app.data.travel.TravelCreateRequest
import com.plango.app.data.travel.TravelDetailResponse
import com.plango.app.data.travel.TravelSummaryResponse
import com.plango.app.data.user.UserReadResponse
import com.plango.app.data.user.UserRequest
import com.plango.app.data.user.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {



    @POST("/api/users/custom")
    suspend fun createUser(
        @Body request : UserRequest
    ) : UserResponse
    @GET("/api/users/{publicId}")
    suspend fun getUser(
        @Path("publicId") publicId: String
    ): UserReadResponse
    @POST("/api/travels/create")
    suspend fun createTravel(
        @Body request: TravelCreateRequest
    ): TravelDetailResponse

    //  다가올 여행 목록
    @GET("/api/travels/read/upcoming/{publicId}")
    suspend fun getUpcomingTravels(
        @Path("publicId") publicId: String
    ): List<TravelSummaryResponse>

    //  지난 여행 목록
    @GET("/api/travels/read/finished/{publicId}")
    suspend fun getFinishedTravels(
        @Path("publicId") publicId: String
    ): List<TravelSummaryResponse>

    //  진행 중인 여행 목록
    @GET("/api/travels/read/ongoing/{publicId}")
    suspend fun getOngoingTravels(
        @Path("publicId") publicId: String
    ): List<TravelSummaryResponse>

    //  특정 여행 상세 조회
    @GET("/api/travels/read/{travelId}")
    suspend fun getTravelDetail(
        @Path("travelId") travelId: Long
    ): TravelDetailResponse
}