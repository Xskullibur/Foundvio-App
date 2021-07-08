package com.foundvio.setup

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.foundvio.model.Trackee
import com.foundvio.service.UserService
import com.foundvio.utils.isSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.foundvio.model.User
import com.foundvio.service.TrackerTrackeeService
import dagger.hilt.android.AndroidEntryPoint

@HiltViewModel
class SetupViewModel @Inject constructor(
    @ApplicationContext var context: Context,
    var userService: UserService,
    var trackerTrackeeService: TrackerTrackeeService
): ViewModel() {

    var isTrackee = false
    private val _trackees = MutableLiveData<MutableList<User>>(mutableListOf())
    val trackees: LiveData<MutableList<User>> get() = _trackees

    /**
     * Add [Trackee] into the list of Trackees [LiveData]
     */
    fun addTrackee(trackee: User) {

        viewModelScope.launch {
            val response = trackerTrackeeService.addTrackerTrackee(trackee.phone)
            if (response.isSuccess()){
                Toast.makeText(context, "Added Trackee", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Error: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
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
                val response = userService.registerUser(isTrackee)
                if(response.isSuccess()){
                    Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
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