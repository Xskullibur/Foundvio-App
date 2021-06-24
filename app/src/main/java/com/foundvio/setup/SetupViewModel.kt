package com.foundvio.setup

import androidx.lifecycle.*
import com.foundvio.model.Trackee

class SetupViewModel: ViewModel() {

    private val _trackees = MutableLiveData<MutableList<Trackee>>(mutableListOf())
    val trackees: LiveData<MutableList<Trackee>> get() = _trackees

    fun addTrackee(trackee: Trackee){
        _trackees.value?.add(trackee)
        _trackees.value = _trackees.value
    }

}