package com.foundvio.utils

import retrofit2.Response

class ServerResponse(val message: String, val status: String)


fun Response<ServerResponse>.isSuccess(): Boolean {
    this.body()?.let {
        return it.status == "Success"
    }
    return false
}

fun Response<ServerResponse>.isError(): Boolean {
    this.body()?.let {
        return it.status == "Error"
    }
    return false
}