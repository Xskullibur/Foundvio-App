package com.foundvio.tracking

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class TrackeeGeoCoordProducer {
    private val _geoCoordState = MutableStateFlow(GeoCoord(0.toBigDecimal(), 0.toBigDecimal()))

    /**
     * Provides a [StateFlow] events stream of [GeoCoord]
     *
     * Use [StateFlow.collect] to consume events generated from the producer
     *
     * @see [Flow](https://developer.android.com/kotlin/flow)
     */
    val geoCoordState: StateFlow<GeoCoord> = _geoCoordState


    /**
     * Start tracking [Trackee] and get its [GeoCoord] location
     */
    fun startTracking() {
        start(_geoCoordState)
    }

    /**
     * Stop tracking [Trackee]
     */
    fun stopTracking() {
        stop()
    }


    /**
     * Start listening for tracking updates
     */
    abstract fun start(geoCoordState: MutableStateFlow<GeoCoord>)
    /**
     * Stop listening for tracking updates
     */
    abstract fun stop()
}