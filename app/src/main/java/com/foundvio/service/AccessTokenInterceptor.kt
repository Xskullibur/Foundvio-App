package com.foundvio.service

import okhttp3.Interceptor
import okhttp3.Response

/**
 * This [Interceptor] intercepts all http calls made using Retrofit/OkHttpClient provided in
 * [FoundvioAPIModule] and injects a http header 'access-token' to make sure the client is able to
 * access the server resource backed by the Huawei AuthKit.
 *
 * If an invalid access token is provided, the server may return the following message:
 *  ```
 *  {
 *
 *      "status": "error",
 *      "message": "Unable to verify access token"
 *  }
 *  ```
 */
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