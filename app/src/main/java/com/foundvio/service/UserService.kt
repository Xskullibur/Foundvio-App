package com.foundvio.service

import com.foundvio.utils.ServerResponse
import retrofit2.Response
import javax.inject.Inject

interface UserService {
    suspend fun getIndex(): Response<ServerResponse>
}

class UserServiceImpl @Inject constructor(
    val retrofitUserService: RetrofitUserService
): UserService {
    override suspend fun getIndex(): Response<ServerResponse> {
        return retrofitUserService.getIndex()
    }

}