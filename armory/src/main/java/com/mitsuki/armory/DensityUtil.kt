package com.mitsuki.armory

import android.content.res.Resources

fun dp2px(dpValue: Float): Int {
    return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
}

fun px2dp(pxValue: Float): Float {
    return pxValue / Resources.getSystem().displayMetrics.density
}