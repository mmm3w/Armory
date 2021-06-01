package com.mitsuki.armory.adapter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayDeque


@Suppress("MemberVisibilityCanBePrivate")
abstract class CoroutineUpdateQueueAdapter<T, VH : RecyclerView.ViewHolder>(
    private val diffCallback: DiffUtil.ItemCallback<T>,
    val applyAllUpdate: Boolean
) :
    RecyclerView.Adapter<VH>() {

    protected val mData = arrayListOf<T>()

    private val pendingUpdates: ArrayDeque<List<T>> = ArrayDeque()

    override fun getItemCount(): Int = mData.size

    fun postUpdate(lifecycle: Lifecycle, data: List<T>) {
        lifecycle.coroutineScope.launch { postUpdate(data) }
    }

    suspend fun postUpdate(data: List<T>) {
        if (applyAllUpdate) pendingUpdates.addFirst(data) else pendingUpdates.addLast(data)
        if (pendingUpdates.size > 1) return
        updateData(data)
    }

    private suspend fun updateData(data: List<T>) {
        withContext(Dispatchers.Main) {
            when {
                mData.isEmpty() && data.isNotEmpty() -> {
                    mData.addAll(data)
                    notifyItemRangeInserted(0, data.size)
                }
                mData.isNotEmpty() && data.isEmpty() -> {
                    val count = mData.size
                    mData.clear()
                    notifyItemRangeRemoved(0, count)
                }
                mData.isEmpty() && data.isEmpty() -> {

                }
                else -> {
                    val diff = withContext(Dispatchers.Default) {
                        calculateDiff(diffCallback, mData, data)
                    }
                    mData.clear()
                    mData.addAll(data)
                    diff.dispatchUpdatesTo(this@CoroutineUpdateQueueAdapter)
                }
            }
        }

        pendingUpdates.removeLast()
        if (pendingUpdates.isNotEmpty()) updateData(pendingUpdates.last())
    }
}