package com.mitsuki.armory.httprookie

import com.mitsuki.armory.httprookie.request.GetRequest
import com.mitsuki.armory.httprookie.request.PostRequest
import com.mitsuki.armory.httprookie.request.Request
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpRookie {
    val client by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            //TODO：这里的dns解析存在问题，日后需要解决
            .build()
    }

//    fun <T>get(url:String){
//        GetRequest<T>(url).enqueue()
//    }

    fun <T> get(url: String, func: (Request<T>.() -> Unit)? = null): Request<T> =
        GetRequest<T>(url).apply { func?.let { this.it() } }


    fun <T> post(url: String, func: (Request<T>.() -> Unit)? = null): Request<T> =
        PostRequest<T>(url).apply { func?.let { this.it() } }
}
