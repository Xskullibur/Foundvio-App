package com.foundvio.service

import com.foundvio.model.User
import com.foundvio.setup.SetupUserDetails
import com.foundvio.utils.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @GET(".")
    suspend fun getIndex(): Response<ServerResponse<String>>

    @POST("registerUser")
    suspend fun registerUser(@Body user: SetupUserDetails): Response<ServerResponse<String>>

    @GET("userDetails")
    suspend fun userDetails(): Response<ServerResponse<User>>

}