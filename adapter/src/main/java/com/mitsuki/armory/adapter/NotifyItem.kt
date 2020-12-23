package com.mitsuki.armory.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

sealed class NotifyItem {
    abstract fun dispatch(adapter: RecyclerView.Adapter<*>)

    //填充新数据
    class NewData(private val mSize: Int) : NotifyItem() {
        override fun dispatch(adapter: RecyclerView.Adapter<*>) {
            adapter.notifyItemRangeInserted(0, mSize)
        }
    }

    //刷新数据
    class RefreshData(private val diffResult: DiffUtil.DiffResult) : NotifyItem() {
        override fun dispatch(adapter: RecyclerView.Adapter<*>) {
            diffResult.dispatchUpdatesTo(adapter)
        }
    }

    //加载更多
    class LoadData(private val mStart: Int, private val mSize: Int) : NotifyItem() {
        override fun dispatch(adapter: RecyclerView.Adapter<*>) {
            adapter.notifyItemRangeInserted(mStart, mSize)
        }

    }

    //清空数据
    class ClearData(private val mSize: Int) : NotifyItem() {
        override fun dispatch(adapter: RecyclerView.Adapter<*>) {
            adapter.notifyItemRangeRemoved(0, mSize)
        }

    }

    //更新指定数据
    class UpdateData(private val position: Int) : NotifyItem() {
        override fun dispatch(adapter: RecyclerView.Adapter<*>) {
            adapter.notifyItemChanged(position)
        }
    }
}