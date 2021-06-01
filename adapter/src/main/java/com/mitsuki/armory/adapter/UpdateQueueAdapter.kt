package com.mitsuki.armory.adapter

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayDeque

abstract class UpdateQueueAdapter<T, VH : RecyclerView.ViewHolder>(
    private val diffCallback: DiffUtil.ItemCallback<T>,
    private val applyAllUpdate: Boolean
) :
    RecyclerView.Adapter<VH>() {

    private val mDelivery: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val workThread: HandlerThread by lazy { HandlerThread("UpdateQueueAdapter") }

    init {
        workThread.start()
    }

    protected val mData = arrayListOf<T>()

    private val pendingUpdates: ArrayDeque<List<T>> = ArrayDeque()

    override fun getItemCount(): Int = mData.size

    fun postUpdate(data: List<T>) {
        if (applyAllUpdate) pendingUpdates.addFirst(data) else pendingUpdates.addLast(data)
        if (pendingUpdates.size > 1) return
        updateData(data)
    }

    private fun updateData(data: List<T>) {
        when {
            mData.isEmpty() && data.isNotEmpty() ->
                applyNotify(NotifyItem.NewData(data.size), data)
            mData.isNotEmpty() && data.isEmpty() ->
                applyNotify(NotifyItem.ClearData(mData.size), data)
            mData.isEmpty() && data.isEmpty() -> applyNotify(null, data)
            else -> {
                workThread.run {
                    val diff = calculateDiff(diffCallback, mData, data)
                    mDelivery.run { applyNotify(NotifyItem.RefreshData(diff), data) }
                }
            }
        }
    }

    private fun applyNotify(notifyItem: NotifyItem?, data: List<T>) {
        when (notifyItem) {
            is NotifyItem.NewData -> mData.addAll(data)
            is NotifyItem.ClearData -> mData.clear()
            is NotifyItem.RefreshData -> {
                mData.clear()
                mData.addAll(data)
            }
            else -> {
            }
        }
        notifyItem?.dispatch(this)

        pendingUpdates.removeLast()
        if (pendingUpdates.isNotEmpty()) updateData(pendingUpdates.last())
    }
}