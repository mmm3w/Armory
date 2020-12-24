package com.mitsuki.armory.httprookie.observable

import com.mitsuki.armory.httprookie.callback.CallbackConvert
import com.mitsuki.armory.httprookie.convert.Convert
import com.mitsuki.armory.httprookie.response.Response
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.exceptions.Exceptions
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import okhttp3.Call

class EnqueueObservable<T>(private val mRawCall: Call, private val mConvert: Convert<T>) :
    Observable<Response<T?>>() {

    override fun subscribeActual(observer: Observer<in Response<T?>>?) {
        val call = mRawCall.clone()
        val callback = Callback(call, observer, mConvert)
        observer?.onSubscribe(callback)
        call.enqueue(callback)
    }


    class Callback<T>(
        private val mCall: Call,
        private val mObserver: Observer<in Response<T?>>?,
        convert: Convert<T>
    ) :
        Disposable, CallbackConvert<T>(convert) {

        private var mIsTerminated = false


        override fun isDisposed(): Boolean {
            return mCall.isCanceled()
        }

        override fun dispose() {
            mCall.cancel()
        }

        override fun onStart() {
        }

        override fun onSuccess(response: Response.Success<T?>) {
            if (mCall.isCanceled()) return
            try {
                mObserver?.onNext(response)
            } catch (e: Exception) {
                if (mIsTerminated) {
                    RxJavaPlugins.onError(e)
                } else {
                    response.apply { onError(Response.Fail(e, rawCall, rawResponse)) }
                }
            }
        }

        override fun onError(response: Response.Fail<T?>) {
            if (mCall.isCanceled()) return

            try {
                mIsTerminated = true
                mObserver?.onError(response.throwable)
            } catch (inner: Throwable) {
                Exceptions.throwIfFatal(inner)
                RxJavaPlugins.onError(CompositeException(response.throwable, inner))
            }
        }

        override fun onFinish() {
            if (mCall.isCanceled()) return

            try {
                mIsTerminated = true
                mObserver?.onComplete()
            } catch (inner: Throwable) {
                Exceptions.throwIfFatal(inner)
                RxJavaPlugins.onError(inner)
            }
        }
    }

}