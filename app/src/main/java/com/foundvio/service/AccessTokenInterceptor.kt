package com.foundvio.service

import okhttp3.Interceptor
import okhttp3.Response


class AccessTokenInterceptor: Interceptor {

    var accessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = accessToken
        val request = chain.request()

        return if(accessToken != null){
            val newRequest = request.newBuilder()
                .addHeader("access-token", accessToken)
                .build()
            return chain.proceed(newRequest)
        }else{
            chain.proceed(request)
        }
    }
}