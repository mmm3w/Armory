package com.mitsuki.armory.extend

import android.content.Context
import android.content.res.Resources

fun dp2px(dpValue: Float): Int {
    return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
}

fun px2dp(pxValue: Float): Float {
    return pxValue / Resources.getSystem().displayMetrics.density
}

fun statusBarHeight(context: Context): Int {
    val id = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (id > 0)
        return context.resources.getDimensionPixelSize(id)
    return 0
}

fun navigationBarHeight(context: Context): Int {
    val id = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (id > 0)
        return context.resources.getDimensionPixelSize(id)
    return 0
}