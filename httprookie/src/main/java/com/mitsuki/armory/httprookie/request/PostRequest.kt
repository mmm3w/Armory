package com.mitsuki.armory.httprookie.request

import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

class PostRequest<T>(url: String) : Request<T>(url), HasBody {
    override var type: HasBody.Type = HasBody.Type.NONE

    override var mediaType: MediaType? = null
    override lateinit var requestBody: RequestBody
    override lateinit var content: String
    override lateinit var bs: ByteArray
    override lateinit var file: File

    override fun generateRequest(): okhttp3.Request {
        return generateRequestBuilder().tag(tag).post(generateRequestBody()).url(url()).build()
    }

    override fun generateRequestBuilder(): okhttp3.Request.Builder {
        return okhttp3.Request.Builder()
    }


}