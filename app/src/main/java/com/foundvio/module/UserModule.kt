package com.foundvio.module

import com.foundvio.service.UserService
import com.foundvio.service.UserServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {


    @Binds
    abstract fun bindUserService(userServiceImpl: UserServiceImpl): UserService

}