package com.mitsuki.armory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class BaseAdapter<B, T : RecyclerView.ViewHolder>(private val mData: MutableList<B>? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val VIEW_TYPE_EMPTY = Integer.MIN_VALUE

    private var emptyLayout: Int = -1
    private var useEmpty = false

    override fun getItemCount(): Int {
        with(getDataCount()) {
            return if (this <= 0 && useEmpty && emptyLayout != -1) 1 else this
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (emptyEnable()) VIEW_TYPE_EMPTY
        else 0
    }

    final override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return if (getItemViewType(i) == VIEW_TYPE_EMPTY && emptyLayout != -1 && useEmpty) {
            onCreateEmptyViewHolder(viewGroup, i)
        } else onMyCreateViewHolder(viewGroup, i)
    }

    final override fun onBindViewHolder(t: RecyclerView.ViewHolder, i: Int) {
        if (getItemViewType(i) == VIEW_TYPE_EMPTY) {
            onBindEmptyViewHolder(t, i)
        } else {
            onMyBindViewHolder(t as T, i, getItem(i))
        }
    }

    abstract fun onMyCreateViewHolder(viewGroup: ViewGroup, i: Int): T

    abstract fun onMyBindViewHolder(t: T, i: Int, item: B?)

    open fun onCreateEmptyViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        with(LayoutInflater.from(viewGroup.context).inflate(emptyLayout, viewGroup, false)) {
            return object : RecyclerView.ViewHolder(this) {}
        }
    }

    open fun onBindEmptyViewHolder(t: RecyclerView.ViewHolder, i: Int) {

    }

    private fun emptyEnable(): Boolean {
        return getDataCount() <= 0 && useEmpty && emptyLayout != -1
    }

    private fun getDataCount(): Int {
        with(mData?.run { size } ?: 0) { return this }
    }

    private fun getItem(position: Int): B? {
        return mData?.get(position)
    }

    fun setEmptyLayout(emptyLayout: Int) {
        this.emptyLayout = emptyLayout
    }

    fun setUseEmpty(useEmpty: Boolean) {
        this.useEmpty = useEmpty
    }
}

fun <V : View> RecyclerView.ViewHolder.view(@IdRes id: Int): V? {
    return itemView.findViewById(id)
}
