package com.mitsuki.armory.httprookie.response

/**
 * 通过enqueueObservable()使用时
 * 需要留意该类型的异常处理
 * 所有的元数据都会在response中
 */
class MetaThrowable(val failResponse: Response.Fail<*>, cause: Throwable) : Throwable(cause) {
    fun directMessage() = failResponse.throwable.message ?: ""
}