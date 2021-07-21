package com.foundvio.service

import com.foundvio.model.User
import com.foundvio.utils.ServerResponse
import com.huawei.agconnect.credential.Server
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Inject

// Services Network Calls
interface TrackerTrackeeService {

    /*
    Functions needs to be suspended so all network calls on background thread
     */

    @GET("getTrackeesByTrackerId")
    suspend fun getTrackeesByTrackerId(): Response<ServerResponse<MutableList<User>>>

    @POST("addTrackerTrackee")
    suspend fun addTrackerTrackee(@Body trackeeId: Long): Response<ServerResponse<String>>

    @POST("removeTrackerTrackee")
    suspend fun removeTrackerTrackee(@Body trackeeId: Long): Response<ServerResponse<String>>
}