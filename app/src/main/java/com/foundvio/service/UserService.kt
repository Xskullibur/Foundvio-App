package com.foundvio.service

import com.foundvio.model.User
import com.foundvio.utils.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @GET(".")
    suspend fun getIndex(): Response<ServerResponse>

    @POST("addUser")
    suspend fun addUser(): Response<ServerResponse>

}