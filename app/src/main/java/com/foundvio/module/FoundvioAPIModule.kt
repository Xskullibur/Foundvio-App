package com.foundvio.module

import com.foundvio.service.AccessTokenInterceptor
import com.foundvio.service.UserService
import com.foundvio.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CookieJar
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FoundvioAPIModule {

    @Provides
    fun provideServerHost() = "http://192.168.1.1:8080/"

    @Singleton
    @Provides
    fun provideAccessTokenInterceptor(): AccessTokenInterceptor {
        return AccessTokenInterceptor()
    }

    @Singleton
    @Provides
    fun provideCookieJar(): CookieJar {
        val cookieManager = CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }
        return JavaNetCookieJar(cookieManager)
    }

    @Singleton
    @Provides
    fun provideFoundvioOkHttpClient(
        cookieJar: CookieJar,
        accessTokenInterceptor: AccessTokenInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
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
    fun provideUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Singleton
    @Provides
    fun provideTrackerTrackeeService(retrofit: Retrofit) = retrofit.create(TrackerTrackeeService::class.java)

}