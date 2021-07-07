package com.foundvio.login

import androidx.lifecycle.ViewModel
import com.foundvio.service.AccessTokenInterceptor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val accessTokenInterceptor: AccessTokenInterceptor
) :ViewModel(){

    var isRegister = false

    fun setAccessToken(accessToken: String){
        accessTokenInterceptor.accessToken = accessToken
    }

}