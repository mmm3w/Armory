package com.mitsuki.loadprogress

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

object ProgressProvider {

    private const val URL_MARK = "MTKMark"
    private const val MARK_FORMAT = "$URL_MARK=%s:"

    private val mProgressSubject: PublishSubject<Progress> by lazy { PublishSubject.create() }

    val imageLoadInterceptor = LoadInterceptor(mProgressSubject)

    fun event(tag: String): Observable<Progress> =
        mProgressSubject.hide().filter { it.tag == tag }.observeOn(AndroidSchedulers.mainThread())

    fun decorateUrl(url: String, tag: String): String {
        return String.format(MARK_FORMAT, tag) + url
    }

    fun cleanUrl(url: String): Pair<String, String> {
        return if (url.contains(URL_MARK))
            url.substring(url.indexOf(":") + 1) to url.substring(0, url.indexOf(":")).substring(8)
        else
            url to ""
    }
}

fun String.addFeature(tag: String): String = ProgressProvider.decorateUrl(this, tag)

fun String.clearFeature(): Pair<String, String> = ProgressProvider.cleanUrl(this)