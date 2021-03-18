package com.mitsuki.loadprogress

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

object ProgressProvider {

    private const val URL_MARK = "MTKMark"

    var customMark: String? = null

    private val mProgressSubject: PublishSubject<Progress> by lazy { PublishSubject.create() }

    val imageLoadInterceptor = LoadInterceptor(mProgressSubject)

    fun event(tag: String): Observable<Progress> =
        mProgressSubject.hide().filter { it.tag == tag }.observeOn(AndroidSchedulers.mainThread())

    fun decorateUrl(url: String, tag: String): String {
        if (customMark.isNullOrEmpty()) return url + URL_MARK + tag + URL_MARK
        return url + customMark + tag + customMark
    }

    fun cleanUrl(url: String): Pair<String, String> {
        val mark: String = if (customMark.isNullOrEmpty()) URL_MARK else (customMark ?: URL_MARK)
        return if (url.contains(mark))
            url.removeRange(url.indexOf(mark), url.lastIndexOf(mark)).replace(mark, "") to
                    url.substring(url.indexOf(mark) + mark.length, url.lastIndexOf(mark))
        else
            url to ""
    }
}

fun String.addFeature(tag: String): String = ProgressProvider.decorateUrl(this, tag)

fun String.clearFeature(): Pair<String, String> = ProgressProvider.cleanUrl(this)