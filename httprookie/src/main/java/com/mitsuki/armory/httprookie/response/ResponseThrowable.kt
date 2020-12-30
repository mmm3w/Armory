package com.mitsuki.armory.httprookie.response

class ResponseThrowable(val code: Int, msg: String? = null, cause: Throwable? = null) : Throwable(msg, cause)