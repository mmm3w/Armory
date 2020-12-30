package com.mitsuki.armory.httprookie

import android.os.Handler
import android.os.Looper
import com.mitsuki.armory.httprookie.request.*
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpRookie : UrlParams, Headers {

    //公共url参数
    override val urlParams: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()
    //公共header
    override val headers: LinkedHashMap<String, String> = LinkedHashMap()

    private val mDelivery = Handler(Looper.getMainLooper())

    val client by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }


    fun <T> get(url: String, func: (GetRequest<T>.() -> Unit)? = null): GetRequest<T> =
        GetRequest<T>(url).apply { func?.let { this.it() } }

    fun <T> post(url: String, func: (PostRequest<T>.() -> Unit)? = null): PostRequest<T> =
        PostRequest<T>(url).apply { func?.let { this.it() } }


    fun runOnUiThread(run: Runnable) {
        mDelivery.post(run)
    }

    fun cancel(tag: Any) {
        TODO()
    }
}
