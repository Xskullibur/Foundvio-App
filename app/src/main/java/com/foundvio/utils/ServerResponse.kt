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
class ServerResponse(val message: String, val status: String)

/**
 * Check if server response a successful message
 */
fun Response<ServerResponse>.isSuccess(): Boolean {
    this.body()?.let {
        return it.status == "Success"
    }
    return false
}

/**
 * Check if server response an error message
 */
fun Response<ServerResponse>.isError(): Boolean {
    this.body()?.let {
        return it.status == "Error"
    }
    return false
}