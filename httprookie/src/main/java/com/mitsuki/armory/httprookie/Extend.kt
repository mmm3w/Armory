package com.mitsuki.armory.httprookie

import okhttp3.Call
import okhttp3.Response
import java.io.IOException

fun Call.enqueueBy(
    onResponse: ((call: Call, response: Response) -> Unit)? = null,
    onFailure: ((call: Call, e: IOException) -> Unit)? = null
) {
    enqueue(object : okhttp3.Callback {
        override fun onResponse(call: Call, response: Response) {
            onResponse?.invoke(call, response)
        }

        override fun onFailure(call: Call, e: IOException) {
            onFailure?.invoke(call, e)
        }
    })
}