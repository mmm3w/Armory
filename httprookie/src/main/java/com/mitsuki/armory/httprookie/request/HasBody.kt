package com.mitsuki.armory.httprookie.request

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URLConnection

interface HasBody {
    var type: Type
    var mediaType: MediaType?
    var requestBody: RequestBody
    var content: String
    var bs: ByteArray
    var file: File


    enum class Type {
        STRING, JSON, BYTES, FILE, CUSTOM, NONE
    }
}

fun HasBody.generateRequestBody(): RequestBody? {
    return when (type) {
        HasBody.Type.STRING, HasBody.Type.JSON -> content.toRequestBody(mediaType)
        HasBody.Type.BYTES -> bs.toRequestBody(mediaType)
        HasBody.Type.FILE -> file.asRequestBody(mediaType)
        HasBody.Type.CUSTOM -> requestBody
        HasBody.Type.NONE -> null
    }
}

fun HasBody.string(data: String, mediaType: MediaType? = null) {
    this.type = HasBody.Type.STRING
    this.content = data
    this.mediaType = mediaType
}

fun HasBody.file(data: File, mediaType: MediaType? = null) {

}

fun HasBody.bytes(data: ByteArray) {

}

fun HasBody.json(data: String) {

}

fun HasBody.requestBody(data: RequestBody) {
    this.type = HasBody.Type.CUSTOM
    this.requestBody = data
}