package com.mitsuki.armory.httprookie.callback

import com.mitsuki.armory.httprookie.convert.Convert
import com.mitsuki.armory.httprookie.response.Response
import okhttp3.Call
import java.io.IOException

class DefaultCallbackConvert<T>(
    convert: Convert<T>,
    var callback: Callback<T> = DefaultCallback()
) : CallbackConvert<T>(convert) {
    override fun onStart() = callback.onStart()

    override fun onSuccess(response: Response.Success<T?>) = callback.onSuccess(response)

    override fun onError(response: Response.Fail<T?>) = callback.onError(response)

    override fun onFinish() = callback.onFinish()
}