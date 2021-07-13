package com.foundvio.service

import com.foundvio.utils.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject

// Services Network Calls
interface TrackerTrackeeService {

    /*
    Functions needs to be suspended so all network calls on background thread
     */

    @POST("addTrackerTrackee")
    suspend fun addTrackerTrackee(@Body trackeeId: Long): Response<ServerResponse<String>>
}