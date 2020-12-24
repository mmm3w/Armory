package com.mitsuki.armory.httprookie.request

import com.mitsuki.armory.httprookie.HttpRookie
import com.mitsuki.armory.httprookie.callback.Callback
import com.mitsuki.armory.httprookie.callback.DefaultCallbackConvert
import com.mitsuki.armory.httprookie.callback.DefaultCallback
import com.mitsuki.armory.httprookie.convert.Convert
import com.mitsuki.armory.httprookie.observable.EnqueueObservable
import com.mitsuki.armory.httprookie.response.Response
import io.reactivex.rxjava3.core.Observable
import okhttp3.Call
import okhttp3.Request

abstract class Request<T>(val url: String) {
    //TODO 初始化检查
    lateinit var convert: Convert<T>

    private var mRequest: Request? = null

    private val mCallbackConvert: DefaultCallbackConvert<T> by lazy {
        DefaultCallbackConvert(convert)
    }

    fun header(header: Pair<String, String>) {

    }

    fun params(param: Pair<String, String>) {

    }

    fun tag(tag: Any) {

    }

    fun callback(
        onStart: (() -> Unit)? = null,
        onSuccess: ((response: Response.Success<T?>) -> Unit)? = null,
        onError: ((response: Response.Fail<T?>) -> Unit)? = null,
        onFinish: (() -> Unit)? = null
    ) {
        mCallbackConvert.callback = DefaultCallback(onStart, onSuccess, onError, onFinish)
    }

    fun callback(callback: Callback<T>) {
        mCallbackConvert.callback = callback
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
        generateCall().enqueue(mCallbackConvert)
    }


    //同步调用
    fun execute() {

    }

    fun enqueueObservable(): Observable<Response<T?>> {
        return EnqueueObservable(generateCall(), convert)
    }
}