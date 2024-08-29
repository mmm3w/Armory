package com.mitsuki.armory.adapter.data

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.recyclerview.widget.DiffUtil
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class GeneralNotifyQueueData<T>(diffCallback: DiffUtil.ItemCallback<T>) :
    NotifyQueueData<T>(diffCallback), Runnable {

    private val pendingUpdates: BlockingQueue<NotifyData<T>> = LinkedBlockingQueue()

//    private val sPoolSync: Any = Any()

    private val mWorkThread: HandlerThread by lazy { HandlerThread("M_NotifyQueueData").apply { start() } }
    private val mHandler = Handler(mWorkThread.looper).apply { post(this@GeneralNotifyQueueData) }
    private val mDelivery: Handler by lazy { Handler(Looper.getMainLooper()) }


    override fun postUpdate(nd: NotifyData<T>) {
        pendingUpdates.add(nd)
    }

    private fun handleNotifyData(nd: NotifyData<T>) {
        requireAdapter()?.also { adapter ->
            nd.applyChange(data, diffCallback, true)
            mDelivery.post { nd.dispatchChange(adapter) }
        } ?: run {
            nd.applyChange(data, diffCallback, false)
        }
    }

    override fun run() {
        var notifyData: NotifyData<T>? = pendingUpdates.take()
        while (notifyData != null) {
            handleNotifyData(notifyData)
            notifyData = pendingUpdates.take()
        }
    }

    override fun release() {
        mWorkThread.quit()
    }
}