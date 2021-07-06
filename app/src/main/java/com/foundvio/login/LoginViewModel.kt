package com.foundvio.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundvio.service.AccessTokenInterceptor
import com.foundvio.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val userService: UserService,
    val accessTokenInterceptor: AccessTokenInterceptor
) :ViewModel(){

    fun getIndex(){
        viewModelScope.launch {
            loadData {
                val response = userService.getIndex()
                println()
            }
        }
    }

    fun setAccessToken(accessToken: String){
        accessTokenInterceptor.accessToken = accessToken
    }


    private val _loading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _loading

    private suspend fun loadData(block: suspend () -> Unit){
        _loading.postValue(true)
        block()
        _loading.postValue(false)
    }

}