package com.foundvio.tracking.testing

import com.foundvio.tracking.GeoCoord
import com.foundvio.tracking.GeoCoord.Companion.at
import com.foundvio.tracking.TrackeeGeoCoordProducer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class RandomTrackeeGeoCoordProducer(val scope: CoroutineScope) : TrackeeGeoCoordProducer(){

    /**
     * Generate a [GeoCoord] from a normal distribution centered around Singapore
     */
    private fun randomGeoCoord(): GeoCoord {
        val random = Random()

        val meanLat = 1.3521
        val meanLng = 103.8198

        val stdDv = .01

        val lat = random.nextGaussian() * stdDv + meanLat
        val lng = random.nextGaussian() * stdDv + meanLng
        return lat at lng
    }

    private val job = Job()

    override fun start(geoCoordState: MutableStateFlow<GeoCoord>) {
        scope.launch(job + Dispatchers.Unconfined) {
            startEventLoop(geoCoordState)
        }
    }

    override fun stop() {
        job.cancel("Stop Tracking")
    }

    private suspend fun startEventLoop(geoCoordState: MutableStateFlow<GeoCoord>){
        while(true){
            delay(1000)
            geoCoordState.emit(randomGeoCoord())
        }
    }


}