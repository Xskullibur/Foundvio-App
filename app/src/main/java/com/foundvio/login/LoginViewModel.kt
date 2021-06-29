package com.foundvio.login

import androidx.lifecycle.ViewModel
import com.foundvio.model.User
import com.foundvio.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    var userService: UserService
) :ViewModel(){

    fun addUser(user: User){
        userService.addUser(user)
    }

}