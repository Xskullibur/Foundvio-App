package com.foundvio.service

import com.foundvio.utils.ServerResponse
import retrofit2.Response
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
    suspend fun addTrackerTrackee(
        @Query("trackeeId") trackeeId: String?
    ): Response<ServerResponse>
}

// Help to access service while maintaining encapsulation
interface TrackerTrackeeHelper {
    suspend fun addTrackerTrackee(trackeeId: String): Response<ServerResponse>
}

// Implements Helper to provide functionality
class TrackerTrackeeHelperImpl @Inject constructor (
    val trackerTrackeeService: TrackerTrackeeService
): TrackerTrackeeHelper {

    override suspend fun addTrackerTrackee(
        trackeeId: String
    ): Response<ServerResponse> {
        return trackerTrackeeService.addTrackerTrackee(trackeeId)
    }
}