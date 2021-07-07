package com.foundvio.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundvio.model.Trackee
import com.foundvio.service.UserService
import com.foundvio.utils.isSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    var userService: UserService
): ViewModel() {

    var isTrackee = false
    var setupUserDetails = SetupUserDetails()


    private val _trackees = MutableLiveData<MutableList<Trackee>>(mutableListOf())
    val trackees: LiveData<MutableList<Trackee>> get() = _trackees

    /**
     * Add [Trackee] into the list of Trackees [LiveData]
     */
    fun addTrackee(trackee: Trackee){
        _trackees.value?.add(trackee)
        _trackees.value = _trackees.value
    }

    /**
     * Register the user with the server
     *
     * NOTE:
     * Only call this method after completing setup process
     */
    fun registerUser(){
        viewModelScope.launch {
            loadData {
                val response = userService.registerUser(
                    isTrackee,
                    setupUserDetails.phone,
                    setupUserDetails.familyName,
                    setupUserDetails.givenName)
                if(response.isSuccess()){
                    _toast.value = "User created"
                }else{
                    _toast.value = response.body()?.message
                }
            }
        }
    }

    //To show toast message
    private val _toast = MutableLiveData<String>(null)
    val toast: LiveData<String?> get() = _toast

    //To determine whether to show a spinner if the app is loading some data
    private val _loading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _loading

    /**
     * Automatically update [isLoading] and shows a spinner
     * when running the suspend [block]
     */
    private suspend fun loadData(block: suspend () -> Unit){
        _loading.postValue(true)
        block()
        _loading.postValue(false)
    }

}