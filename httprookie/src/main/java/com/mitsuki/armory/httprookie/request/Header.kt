package com.mitsuki.armory.httprookie.request

interface Headers {
    val headers: LinkedHashMap<String, String>
}

fun Headers.header(header: Pair<String, String>) {
    if (header.second.isEmpty()) return
    if (header.first.isEmpty()) return
    headers[header.first] = header.second
}

fun Headers.header(data: Headers) {
    if (data.headers.isEmpty()) return
    headers.putAll(data.headers)
}

fun Headers.appendHeaders(builder: okhttp3.Request.Builder): okhttp3.Request.Builder {
    if (headers.isEmpty()) return builder
    return builder.headers(okhttp3.Headers.Builder().apply {
        for (header in headers) add(header.key, header.value)
    }.build())
}

fun Headers.clearHeaders() {
    headers.clear()
}