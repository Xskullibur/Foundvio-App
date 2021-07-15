package com.foundvio.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.foundvio.service.UserService
import com.foundvio.tracking.TrackeeGeoCoordProducer
import com.foundvio.tracking.testing.RandomTrackeeGeoCoordProducer
import com.foundvio.utils.isSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    var userService: UserService,
) : ViewModel() {

    private val trackeeGeoCoordProducer: TrackeeGeoCoordProducer
        = RandomTrackeeGeoCoordProducer(viewModelScope)

    val userLocation = trackeeGeoCoordProducer.geoCoordState.asLiveData()

    fun startTracking(){
        trackeeGeoCoordProducer.startTracking()
    }

    fun stopTracking(){
        trackeeGeoCoordProducer.stopTracking()
    }

    fun userDetails(){
        viewModelScope.launch{
            val res = userService.userDetails()

            if(res.isSuccess()){
                val user = res.body()!!.message
                println(user.id)
            }
        }
    }

}