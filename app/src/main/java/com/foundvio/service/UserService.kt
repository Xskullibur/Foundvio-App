package com.foundvio.service

import com.foundvio.model.User

interface UserService {
    fun addUser(user: User)
}