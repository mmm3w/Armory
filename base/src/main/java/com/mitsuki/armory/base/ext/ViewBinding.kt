package com.mitsuki.armory.base.ext

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

fun <VB : ViewBinding> Activity.viewBinding(inflate: (LayoutInflater) -> VB) = lazy {
    inflate(layoutInflater).apply { setContentView(root) }
}





