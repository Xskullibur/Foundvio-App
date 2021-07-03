package com.foundvio.service

import com.foundvio.utils.ServerResponse
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitUserService {

    @GET(".")
    suspend fun getIndex(): Response<ServerResponse>

}