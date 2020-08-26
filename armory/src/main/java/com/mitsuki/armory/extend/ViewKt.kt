package com.mitsuki.armory.extend

import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView


fun View.paddingVertical() = paddingTop + paddingBottom

fun View.paddingHorizontal() = paddingStart + paddingEnd

fun View.marginVertical() = marginTop + marginBottom

fun View.marginHorizontal() = marginStart + marginEnd

fun RecyclerView.addOnScrollListenerBy(
    onScrollStateChanged: ((recyclerView: RecyclerView, newState: Int) -> Unit)? = null,
    onScrolled: ((recyclerView: RecyclerView, dx: Int, dy: Int) -> Unit)? = null
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            onScrollStateChanged?.invoke(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            onScrolled?.invoke(recyclerView, dx, dy)
        }
    })
}

fun <V : View> RecyclerView.ViewHolder.view(@IdRes id: Int): V? {
    return itemView.findViewById(id)
}