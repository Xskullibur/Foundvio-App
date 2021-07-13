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
import com.foundvio.model.User
import com.foundvio.service.TrackerTrackeeService

@HiltViewModel
class SetupViewModel @Inject constructor(
    var userService: UserService,
    var trackerTrackeeService: TrackerTrackeeService
): ViewModel() {

    //To show toast message
    private val _toast = MutableLiveData<String>(null)
    val toast: LiveData<String?> get() = _toast

    var setupUserDetails = SetupUserDetails()

    private val _trackees = MutableLiveData<MutableList<User>>(mutableListOf())
    val trackees: LiveData<MutableList<User>> get() = _trackees

    /**
     * Add [Trackee] into the list of Trackees [LiveData]
     */
    fun addTrackee(trackeeId: Long) {
        viewModelScope.launch {

            // Verify TrackeeId
            val userResponse = userService.getUserById(trackeeId)
            if (userResponse.isSuccess()) {

                // TODO: Check if record exist
                val trackee = userResponse.body()!!.message

                val response = trackerTrackeeService.addTrackerTrackee(trackeeId)
                if (response.isSuccess()){
                    _trackees.value?.add(trackee)
                    _trackees.value = _trackees.value
                    _toast.value = "Added Trackee"
                }
                else{
                    _toast.value = "Error: ${response.body()?.message}"
                }
            }
            else {
                _toast.value = "Invalid user please try again or check if the user is registered."
            }
        }
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
                val response = userService.registerUser(setupUserDetails)
                if(response.isSuccess()){
                    _toast.value = "User created"
                }else{
                    _toast.value = response.body()?.message!!
                }
            }
        }
    }

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