package com.foundvio.utils

import retrofit2.Response

/**
 * Represent the json message return by the server
 *
 * Example:
 * Successful Message from Server:
 *  ```
 *  {
 *
 *      "status": "Success",
 *      "message": "complete"
 *  }
 *  ```
 *
 *  Error Message from Server:
 *  ```
 *  {
 *
 *      "status": "Error",
 *      "message": "unable to perform this request"
 *  }
 *  ```
 */
class ServerResponse<out T>(val message: T, val status: String)

/**
 * Check if server response a successful message
 */
fun<T> Response<ServerResponse<T>>.isSuccess(): Boolean {
    this.body()?.let {
        return it.status == "Success"
    }
    return false
}

/**
 * Check if server response an error message
 */
fun<T> Response<ServerResponse<T>>.isError(): Boolean {
    this.body()?.let {
        return it.status == "Error"
    }
    return false
}