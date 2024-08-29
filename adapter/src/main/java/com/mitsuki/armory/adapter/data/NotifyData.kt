package com.mitsuki.armory.adapter.data

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

sealed class NotifyData<T> {
    @WorkerThread
    abstract fun applyChange(
        source: MutableList<T>, diffCallback: DiffUtil.ItemCallback<T>, isNeedDispatch: Boolean
    )

    @MainThread
    abstract fun dispatchChange(adapter: RecyclerView.Adapter<*>)

    //插入单个
    class Insert<T>(private val data: T, private val index: Int = -1) : NotifyData<T>() {
        private var position = -1

        @WorkerThread
        override fun applyChange(
            source: MutableList<T>, diffCallback: DiffUtil.ItemCallback<T>, isNeedDispatch: Boolean
        ) {
            if (index < 0) {
                position = source.size
                source.add(data)
            } else {
                position = index
                source.add(index, data)
            }
        }

        @MainThread
        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            if (position >= 0) {
                adapter.notifyItemInserted(position)
            }
        }
    }

    //批量插入
    class RangeInsert<T>(private val data: List<T>, private val index: Int = -1) : NotifyData<T>() {
        private var position = -1
        override fun applyChange(
            source: MutableList<T>, diffCallback: DiffUtil.ItemCallback<T>, isNeedDispatch: Boolean
        ) {
            if (index < 0) {
                position = source.size
                source.addAll(data)
            } else {
                position = index
                source.addAll(index, data)
            }
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            if (position >= 0) {
                adapter.notifyItemRangeInserted(position, data.size)
            }
        }
    }

    class RemoveAt<T>(private val index: Int, private val count: Int = 1) : NotifyData<T>() {

        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            if (count == 1) {
                source.removeAt(index)
            } else if (count > 1) {
                source.subList(index, index + count - 1).clear()
            }
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            if (count == 1) {
                adapter.notifyItemRemoved(index)
            } else if (count > 1) {
                adapter.notifyItemRangeRemoved(index, count)
            }
        }
    }

    class Remove<T>(private val data: T) : NotifyData<T>() {
        private var position = -1

        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            position = source.indexOf(data)
            if (position >= 0) {
                source.removeAt(position)
            }
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            if (position >= 0) {
                adapter.notifyItemRemoved(position)
            }
        }
    }

    class RangeRemove<T>(private val data: List<T>) : NotifyData<T>() {
        private lateinit var diffResult: DiffUtil.DiffResult

        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            if (isNeedDispatch) {
                val raw = ArrayList(source)
                source.removeAll(data)
                diffResult = calculateDiff(diffCallback, raw, source)
            } else {
                source.removeAll(data)
            }
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            if (!this::diffResult.isInitialized) return
            diffResult.dispatchUpdatesTo(adapter)
        }
    }

    class Clear<T> : NotifyData<T>() {
        private var count = 0
        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            count = source.size
            source.clear()
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            if (count > 0) {
                adapter.notifyItemRangeRemoved(0, count)
            }
        }
    }

    class Change<T>(private val index: Int, private val data: T, private val payload: Any? = null) :
        NotifyData<T>() {

        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            source.removeAt(index)
            source.add(index, data)
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            adapter.notifyItemChanged(index, payload)
        }
    }

    class ChangeAt<T>(
        private val index: Int, private val action: (T) -> T, private val payload: Any? = null
    ) : NotifyData<T>() {

        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            val temp = source[index]
            source[index] = action(temp)
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            adapter.notifyItemChanged(index, payload)
        }
    }

    class ChangeIf<T>(private val filter: (T) -> Boolean, private val action: (T) -> T) :
        NotifyData<T>() {
        private lateinit var diffResult: DiffUtil.DiffResult
        private lateinit var newData: List<T>

        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            if (isNeedDispatch) {
                val raw = ArrayList(source)
                source.map { if (filter(it)) action(it) else it }
                diffResult = calculateDiff(diffCallback, raw, source)
            } else {
                source.map { if (filter(it)) action(it) else it }
            }
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            if (!this::diffResult.isInitialized) return
            diffResult.dispatchUpdatesTo(adapter)
        }
    }

    class Refresh<T>(private val newData: List<T>) : NotifyData<T>() {
        private lateinit var diffResult: DiffUtil.DiffResult

        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            if (isNeedDispatch) {
                diffResult = calculateDiff(diffCallback, source, newData)
            }
            source.clear()
            source.addAll(newData)
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            if (!this::diffResult.isInitialized) return
            diffResult.dispatchUpdatesTo(adapter)
        }
    }

    class Move<T>(private val fromPosition: Int, private val toPosition: Int) : NotifyData<T>() {
        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(source, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(source, i, i - 1)
                }
            }
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            adapter.notifyItemMoved(fromPosition, toPosition)
        }
    }

    class MergeChange<T>(private val changes: Array<NotifyData<T>>) : NotifyData<T>() {
        private lateinit var diffResult: DiffUtil.DiffResult

        override fun applyChange(
            source: MutableList<T>,
            diffCallback: DiffUtil.ItemCallback<T>,
            isNeedDispatch: Boolean
        ) {
            if (isNeedDispatch) {
                val raw = ArrayList(source)
                changes.forEach { it.applyChange(source, diffCallback, false) }
                diffResult = calculateDiff(diffCallback, raw, source)
            } else {
                changes.forEach { it.applyChange(source, diffCallback, false) }
            }
        }

        override fun dispatchChange(adapter: RecyclerView.Adapter<*>) {
            if (!this::diffResult.isInitialized) return
            diffResult.dispatchUpdatesTo(adapter)
        }
    }
}