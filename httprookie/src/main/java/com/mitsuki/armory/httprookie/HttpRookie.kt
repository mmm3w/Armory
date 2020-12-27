package com.mitsuki.armory.httprookie

import com.mitsuki.armory.httprookie.request.*
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpRookie : UrlParams,Headers{

    //公共url参数
    override val urlParams: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()
    //公共header
    override val headers: LinkedHashMap<String, String> =  LinkedHashMap()

    //TODO：线程调度的问题还没处理
    //TODO：带请求体的还没封装
    //TODO：okhttp通用的一些配置、拦截器之类的还没处理

    val client by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            //TODO：这里的dns解析存在问题，日后需要解决
            .build()
    }


    fun <T> get(url: String, func: (Request<T>.() -> Unit)? = null): Request<T> =
        GetRequest<T>(url).apply { func?.let { this.it() } }


    fun <T> post(url: String, func: (Request<T>.() -> Unit)? = null): Request<T> =
        PostRequest<T>(url).apply { func?.let { this.it() } }




}
