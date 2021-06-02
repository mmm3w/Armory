package com.mitsuki.armory.adapter.notify.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mitsuki.armory.adapter.notify.NotifyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class NotifyQueueData<T>(private val diffCallback: DiffUtil.ItemCallback<T>) {

    private val mData = arrayListOf<T>()

    val count get() = mData.size

    fun item(index:Int) = mData[index]

    private val pendingUpdates: ArrayDeque<NotifyData<T>> = ArrayDeque()
    private var targetAdapter: WeakReference<RecyclerView.Adapter<*>>? = null

    fun postUpdate(lifecycle: Lifecycle, data: NotifyData<T>) {
        lifecycle.coroutineScope.launch { postUpdate(data) }
    }

    suspend fun postUpdate(data: NotifyData<T>) {
        pendingUpdates.addFirst(data)
        if (pendingUpdates.size > 1) return
        updateData(data)
    }

    private suspend fun updateData(data: NotifyData<T>) {
        withContext(Dispatchers.Main) {
            when (data) {
                is NotifyData.Insert,
                is NotifyData.RangeInsert,
                is NotifyData.RemoveAt,
                is NotifyData.Remove,
                is NotifyData.Change,
                is NotifyData.ChangeAt -> applyNotify(data)
                is NotifyData.RangeRemove,
                is NotifyData.ChangeIf,
                is NotifyData.Refresh -> {
                    withContext(Dispatchers.IO) { data.calculateDiff(mData, diffCallback) }
                    applyNotify(data)
                }
            }
        }
    }

    private suspend fun applyNotify(notifyData: NotifyData<T>) {
        targetAdapter?.get()?.apply { notifyData.dispatchUpdates(mData, this) }
        pendingUpdates.removeLast()
        if (pendingUpdates.isNotEmpty()) updateData(pendingUpdates.last())
    }

    fun attachAdapter(adapter: RecyclerView.Adapter<*>) {
        targetAdapter = WeakReference(adapter)
    }
}