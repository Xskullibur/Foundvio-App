package com.foundvio.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundvio.service.AccessTokenInterceptor
import com.foundvio.service.UserService
import com.foundvio.utils.isSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val userService: UserService,
    val accessTokenInterceptor: AccessTokenInterceptor
) :ViewModel(){

    var isRegister = false

    fun getIndex(){
        viewModelScope.launch {
            loadData {
                val response = userService.getIndex()
                println()
            }
        }
    }

    fun addUser() {
        viewModelScope.launch {
            loadData {
                val response = userService.addUser()
                if(response.isSuccess()){
                    Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
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