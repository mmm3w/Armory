package com.mitsuki.armory.httprookie.callback

import com.mitsuki.armory.httprookie.convert.Convert
import com.mitsuki.armory.httprookie.response.Response
import okhttp3.Call
import java.io.IOException

abstract class CallbackConvert<T>(var convert: Convert<T>) : Callback<T>, okhttp3.Callback {
    override fun onFailure(call: Call, e: IOException) {
        onError(Response.Fail(e, call, null))
    }

    override fun onResponse(call: Call, response: okhttp3.Response) {
        val code = response.code
        if (code == 404 || code >= 500) {
            onError(Response.Fail(RuntimeException("404 or 50x"), call, response))
            onFinish()
            return
        }

        //在这里获取对象
        try {
            convert.convertResponse(response)
                .apply { onSuccess(Response.Success(this, call, response)) }
        } catch (e: Exception) {
            //抓到异常的时候回调error
            onError(Response.Fail(e, call, response))
        }

        onFinish()
    }
}