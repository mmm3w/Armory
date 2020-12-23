package com.mitsuki.armory.httprookie.request

class PostRequest<T>(url: String) : Request<T>(url) {
    override fun generateRequest(): okhttp3.Request {
        return okhttp3.Request.Builder().get().url(url).build()
    }
}