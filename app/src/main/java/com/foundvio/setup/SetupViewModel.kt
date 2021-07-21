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
import com.foundvio.utils.isError

@HiltViewModel
class SetupViewModel @Inject constructor(
    var userService: UserService,
    var trackerTrackeeService: TrackerTrackeeService
): ViewModel() {

    //To show toast message
    private val _toast = MutableLiveData<String?>(null)
    val toast: LiveData<String?> get() = _toast

    /**
     * Clear the message inside toast
     */
    fun clearToast(){
        _toast.postValue(null)
    }

    var setupUserDetails = SetupUserDetails()

    private val _trackees = MutableLiveData<MutableList<User>>(mutableListOf())
    val trackees: LiveData<MutableList<User>> get() = _trackees

    init {
        viewModelScope.launch {
            val response = trackerTrackeeService.getTrackeesByTrackerId()
            if (response.isSuccess()) {
                val trackees = response.body()!!.message
                for (trackee in trackees) {
                    _trackees.value?.add(trackee)
                }
                _trackees.value = _trackees.value
            }
        }
    }

    /**
     * Add [Trackee] into the list of Trackees [LiveData]
     */
    fun addTrackee(trackeeId: Long) {


        viewModelScope.launch {

            // Verify TrackeeId
            val userResponse = userService.getUserById(trackeeId)
            if (userResponse.isSuccess()) {

                // Check if record exist
                val queryResponse = trackerTrackeeService.getTrackeesByTrackerId()
                if (queryResponse.isSuccess()) {

                    // Get Added Trackees
                    val registeredTrackees = queryResponse.body()!!.message

                    // Check if trackeeId has already been added
                    val registeredTrackee = registeredTrackees.find { user ->
                        user.id == trackeeId
                    }
                    if (registeredTrackee == null) {

                        // Trackee not added [Add Trackee]
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

                        // Trackee Added
                        _toast.value = "User already added!"
                    }
                }
                else {
                    _toast.value = "Error: ${queryResponse.body()?.message}"
                }

            }
            else {
                _toast.value = "Invalid user please try again or check if the user is registered."
            }
        }
    }

    /**
     * Delete [Trackee] from trackerTrackee
     */
    fun deleteTrackerTrackee(trackeeId: Long) {
        viewModelScope.launch {
            val response = trackerTrackeeService.removeTrackerTrackee(trackeeId)
            if (response.isError()) {
                _toast.value = "Error: ${response.body()?.message}"
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