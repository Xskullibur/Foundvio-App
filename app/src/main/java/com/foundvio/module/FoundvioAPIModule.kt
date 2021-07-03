package com.foundvio.module

import com.foundvio.service.AccessTokenInterceptor
import com.foundvio.service.RetrofitUserService
import com.foundvio.service.UserService
import com.foundvio.service.UserServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FoundvioAPIModule {

    @Provides
    fun provideServerHost() = "http://192.168.1.1:8080"

    @Singleton
    @Provides
    fun provideAccessTokenInterceptor(): AccessTokenInterceptor {
        return AccessTokenInterceptor()
    }

    @Singleton
    @Provides
    fun provideFoundvioOkHttpClient(accessTokenInterceptor: AccessTokenInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(accessTokenInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, serverHost: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(serverHost)
            .client(okHttpClient)
            .build()


    @Singleton
    @Provides
    fun provideRetrofitUserService(retrofit: Retrofit) = retrofit.create(RetrofitUserService::class.java)

    @Singleton
    @Provides
    fun provideUserService(userServiceImpl: UserServiceImpl): UserService = userServiceImpl

}