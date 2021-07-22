package com.foundvio.tracking

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.foundvio.landing.LandingActivity
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow


class TrackeeGeoCoordService : Service() {

    private var producer: TrackeeGeoCoordProducer? = null
    private var job = SupervisorJob()
    private var scope = CoroutineScope(job + Dispatchers.IO)


    override fun onCreate() {
        super.onCreate()
        producer = TrackeeGeoCoordServiceProducer(
            this,
            scope
        )
        producer?.startTracking()
    }

    override fun onDestroy() {
        super.onDestroy()
        producer?.stopTracking()
        job.cancel(CancellationException("Stop tracking user"))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}

class TrackeeGeoCoordServiceProducer(
    val context: Context,
    val scope: CoroutineScope
) : TrackeeGeoCoordProducer() {

    private val TAG = "TrackeeGeoCoordServiceProducer"

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var settingsClient: SettingsClient? = null

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    private var stateFlow: MutableStateFlow<GeoCoord>? = null

    private fun startRequestingLocationUpdates() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        settingsClient = LocationServices.getSettingsClient(context)

        locationRequest = LocationRequest().apply {
            interval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val locations = locationResult.locations
                    if (locations.isNotEmpty()) {
                        for (location in locations) {
                            Log.i(
                                TAG,
                                "onLocationResult location[Longitude,Latitude,Accuracy]:${location.longitude},${location.latitude},${location.accuracy}"
                            )
                            emitGeoCoord(
                                GeoCoord(
                                    location.longitude.toBigDecimal(),
                                    location.latitude.toBigDecimal()
                                )
                            )
                        }
                    }
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                if (locationAvailability != null) {
                    val flag = locationAvailability.isLocationAvailable
                    Log.i(TAG, "onLocationAvailability isLocationAvailable:$flag")
                }
            }
        }

        try {
            val locationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build()

            settingsClient!!.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    Log.i(TAG, "check location settings success")
                    // Request location updates
                    fusedLocationProviderClient!!.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                        .addOnSuccessListener {
                            Log.i(TAG, "requestLocationUpdatesWithCallback onSuccess")
                        }
                        .addOnFailureListener {
                            Log.e(
                                TAG,
                                "requestLocationUpdatesWithCallback onFailure:${it.message}"
                            )
                        }
                }
                .addOnFailureListener {
                    Log.e(TAG, "checkLocationSetting onFailure:${it.message}")
                    when((it as ApiException).statusCode){
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                Log.e(TAG, "requestLocationUpdatesWithCallback onFailure")
                                val rae = it as ResolvableApiException
                                val resolution = rae.resolution
                                context.startActivity(Intent(context, TrackingResolutionActivity::class.java).apply {
                                    putExtra("resolution", resolution)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                })
//                                rae.startResolutionForResult(
//                                    context as AppCompatActivity,
//                                    0
//                                )
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.e(TAG, "PendingIntent unable to execute request.")
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "requestLocationUpdatesWithCallback exception:${e.message}")
        }

    }

    private fun stopRequestingLocationUpdates() {
        try {
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
                .addOnSuccessListener {
                    Log.i(TAG, "removeLocationUpdatesWithCallback onSuccess")
                }
                .addOnFailureListener {
                    Log.e(TAG, "removeLocationUpdatesWithCallback onFailure:${it.message}")
                }
        } catch (e: Exception) {
            Log.e(TAG, "removeLocationUpdatesWithCallback exception:${e.message}")
        }
    }

    override fun start(geoCoordState: MutableStateFlow<GeoCoord>) {
        this.stateFlow = geoCoordState
        startRequestingLocationUpdates()
    }

    override fun stop() {
        this.stateFlow = null
        stopRequestingLocationUpdates()
    }


    private fun emitGeoCoord(geoCoord: GeoCoord) {
        scope.launch { stateFlow?.emit(geoCoord) }
    }

}
