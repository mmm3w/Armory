package com.mitsuki.armory.httprookie.response

import okhttp3.Call

sealed class Response<T>(
    val rawCall: Call,
    val body: T?,
    val rawResponse: okhttp3.Response?
) {

    class Success<T>(body: T, rawCall: Call, rawResponse: okhttp3.Response) :
        Response<T>(rawCall, body, rawResponse) {

        fun requireBody(): T {
            return body ?: throw IllegalStateException("$this`s content is null")
        }

        fun requireRawResponse(): okhttp3.Response {
            return rawResponse ?: throw IllegalStateException("$this`s rawResponse is null")
        }
    }

    class Fail<T>(val throwable: Throwable, rawCall: Call, rawResponse: okhttp3.Response?) :
        Response<T>(rawCall, null, rawResponse)

}