package com.mitsuki.armory.adapter

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

fun <V : View> RecyclerView.ViewHolder.view(@IdRes id: Int): V? {
    return itemView.findViewById(id)
}

/**
 * how to use
 * val binding by viewBinding(XXXXBinding::bind)
 */
fun <VB : ViewBinding> RecyclerView.ViewHolder.viewBinding(bind: (View) -> VB) = lazy { bind(itemView) }
