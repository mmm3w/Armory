package com.mitsuki.armory.httprookie.observable

import com.mitsuki.armory.httprookie.Mediator
import com.mitsuki.armory.httprookie.callback.Callback
import com.mitsuki.armory.httprookie.response.Response
import com.mitsuki.armory.httprookie.response.ResponseThrowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.exceptions.Exceptions
import io.reactivex.rxjava3.plugins.RxJavaPlugins

internal class EnqueueObservable<T>(private val mMediator: Mediator<T>) :
    Observable<Response<T?>>() {

    override fun subscribeActual(observer: Observer<in Response<T?>>?) {
        val mediator = mMediator.clone()
        val callback = CallbackDisposable(mediator, observer)
        observer?.onSubscribe(callback)
        mediator.execute(callback)
    }

    private class CallbackDisposable<T>(
        private val mMediator: Mediator<in T>,
        private val mObserver: Observer<in Response<T?>>?
    ) :
        Disposable, Callback<T> {

        private var mIsTerminated = false

        override fun isDisposed(): Boolean {
            return mMediator.isCanceled()
        }

        override fun dispose() {
            mMediator.cancel()
        }

        override fun onStart() {
        }

        override fun onSuccess(response: Response.Success<T?>) {
            if (mMediator.isCanceled()) return
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
            if (mMediator.isCanceled()) return

            try {
                mIsTerminated = true
                mObserver?.onError(ResponseThrowable(response.hashCode(), cause = response.throwable))
            } catch (inner: Throwable) {
                Exceptions.throwIfFatal(inner)
                RxJavaPlugins.onError(
                    ResponseThrowable(
                        response.hashCode(), cause = CompositeException(response.throwable, inner)
                    )
                )
            }
        }

        override fun onFinish() {
            if (mMediator.isCanceled()) return

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