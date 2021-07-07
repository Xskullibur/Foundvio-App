package com.foundvio.login

import androidx.lifecycle.ViewModel
import com.foundvio.service.AccessTokenInterceptor
import com.huawei.agconnect.auth.AGConnectAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val accessTokenInterceptor: AccessTokenInterceptor
) :ViewModel(){

    var isRegister = false

    /**
     * Retrieve the 'access-token' needed to access server resource from AuthKit and update
     * [com.foundvio.service.AccessTokenInterceptor.accessToken]
     */
    fun retrieveAccessToken(){
        val accessToken = AGConnectAuth.getInstance().currentUser.getToken(false)?.result?.token
        //Set access token for communicating with server
        accessTokenInterceptor.accessToken = accessToken!!
    }

}