package com.mitsuki.armory.httprookie.request

import com.mitsuki.armory.httprookie.HttpRookie
import com.mitsuki.armory.httprookie.callback.Callback
import com.mitsuki.armory.httprookie.convert.Convert
import com.mitsuki.armory.httprookie.response.Response
import okhttp3.Call
import okhttp3.Request
import java.io.IOException

abstract class Request<T>(val url: String) {
    var callback: Callback<T>? = null
    lateinit var convert: Convert<T>

    private var mRequest: Request? = null


    fun header(header: Pair<String, String>) {
    }

    fun params(param: Pair<String, String>) {
    }

    fun tag(tag: Any) {

    }

    abstract fun generateRequest(): Request

    fun generateCall(): Call {
        return generateRequest().let {
            mRequest = it
            HttpRookie.client.newCall(it)
        }
    }

    //异步回调形式
    fun enqueue() {
        callback?.onStart()
        generateCall().enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                //这里这里回调error
                //也可以在这里进行重试次数判断
                callback?.onError(Response.Fail(e, call, null))
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                if (response.code.let {
                        if (it == 404 || it >= 500) {
                            callback?.onError(
                                Response.Fail(
                                    RuntimeException("network error! http response code is 404 or 5xx!"),
                                    call,
                                    response
                                )
                            )
                            true
                        } else false
                    }) return

                //在这里获取对象
                try {
                    convert.convertResponse(response).apply {
                        callback?.onSuccess(Response.Success(this, call, response))
                    }
                } catch (e: Exception) {
                    //抓到异常的时候回调error
                    callback?.onError(Response.Fail(e, call, response))
                }
            }
        })
    }

    //同步调用
    fun execute() {

    }
}