package com.plango.app.api


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
}