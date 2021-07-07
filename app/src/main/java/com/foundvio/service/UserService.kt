package com.foundvio.service

import com.foundvio.model.User
import com.foundvio.utils.ServerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @GET(".")
    suspend fun getIndex(): Response<ServerResponse<String>>

    @POST("registerUser")
    suspend fun registerUser(@Query("isTracker") isTracker: Boolean,
                            @Query("phone") phone: String,
                            @Query("familyName") familyName: String,
                            @Query("givenName") givenName: String
    ): Response<ServerResponse<String>>

    @GET("userDetails")
    suspend fun userDetails(): Response<ServerResponse<User>>

}