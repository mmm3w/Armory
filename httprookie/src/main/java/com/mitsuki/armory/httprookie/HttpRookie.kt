package com.mitsuki.armory.httprookie

import android.os.Handler
import android.os.Looper
import com.mitsuki.armory.httprookie.request.*
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpRookie : UrlParams, Headers {

    //公共url参数
    override val urlParams: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()
        get() {
            if (hasNewUrlParams) {
                field.clear()
                field.apply(commonUrlParams)
                hasNewUrlParams = false
            }
            return field
        }

    //公共header
    override val headers: LinkedHashMap<String, String> = LinkedHashMap()
        get() {
            if (hasNewHeaders) {
                field.clear()
                field.apply(commonHeaders)
                hasNewHeaders = false
            }
            return field
        }

    @Volatile
    var hasNewUrlParams = true

    @Volatile
    var hasNewHeaders = true

    private var commonUrlParams: LinkedHashMap<String, MutableList<String>>.() -> Unit = {}
    private var commonHeaders: LinkedHashMap<String, String>.() -> Unit = {}

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

    fun common(
        commonUrlParams: LinkedHashMap<String, MutableList<String>>.() -> Unit = {},
        commonHeaders: LinkedHashMap<String, String>.() -> Unit = {}
    ) {
        this.commonUrlParams = commonUrlParams
        this.commonHeaders = commonHeaders
    }

    fun cancel(tag: Any) {
        TODO()
    }
}
