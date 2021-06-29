package com.foundvio.module

import com.foundvio.service.UserService
import com.foundvio.service.UserServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class, ViewModelComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindUserService(userServiceImpl: UserServiceImpl): UserService

}