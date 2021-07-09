package com.foundvio.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundvio.service.UserService
import com.foundvio.utils.isSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    var userService: UserService
) : ViewModel() {

    fun userDetails(){
        viewModelScope.launch{
            val res = userService.userDetails()

            if(res.isSuccess()){
                val user = res.body()!!.message
                println(user.id)
            }
        }
    }

}