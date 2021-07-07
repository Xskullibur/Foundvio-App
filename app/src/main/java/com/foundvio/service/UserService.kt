package com.foundvio.service

import com.foundvio.utils.ServerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @GET(".")
    suspend fun getIndex(): Response<ServerResponse>

    @POST("registerUser")
    suspend fun registerUser(@Query("isTrackee") isTrackee: Boolean): Response<ServerResponse>

}