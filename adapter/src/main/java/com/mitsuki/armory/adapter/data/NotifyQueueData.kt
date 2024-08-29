package com.mitsuki.armory.adapter.data

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

abstract class NotifyQueueData<T>(val diffCallback: DiffUtil.ItemCallback<T>) {

    val data: MutableList<T> = arrayListOf()
    val size get() = data.size
    val isEmpty get() = size == 0
    fun index(index: Int) = data[index]


    private var mTargetAdapter: WeakReference<RecyclerView.Adapter<*>>? = null
    fun requireAdapter(): RecyclerView.Adapter<*>? {
        return mTargetAdapter?.get()
    }

    abstract fun postUpdate(nd: NotifyData<T>)

    abstract fun release()
}