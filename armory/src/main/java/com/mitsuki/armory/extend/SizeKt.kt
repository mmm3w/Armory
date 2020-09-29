package com.mitsuki.armory.extend

import android.content.Context
import android.content.res.Resources

fun dp2px(dpValue: Float): Int {
    return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
}

fun px2dp(pxValue: Float): Float {
    return pxValue / Resources.getSystem().displayMetrics.density
}

fun Context.statusBarHeight(): Int {
    val id = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (id > 0)
        return resources.getDimensionPixelSize(id)
    return 0
}

fun Context.navigationBarHeight(): Int {
    val id = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (id > 0)
        return resources.getDimensionPixelSize(id)
    return 0
}