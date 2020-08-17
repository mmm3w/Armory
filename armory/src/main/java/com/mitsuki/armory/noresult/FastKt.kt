package com.mitsuki.armory.noresult

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.enums.RouteType
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.facade.service.PretreatmentService
import com.alibaba.android.arouter.launcher.ARouter


fun Fragment.startForResult(
    intent: Intent, requestCode: Int,
    callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
) {
    NoResult.startActivityForResult(this, intent, requestCode, callback)
}

fun FragmentActivity.startForResult(
    intent: Intent, requestCode: Int,
    callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
) {
    NoResult.startActivityForResult(this, intent, requestCode, callback)
}

/** ARouter Kt ************************************************************************************/
/** 不支持callback 不支持拦截器 不支持动画 不考虑非activity的context跳转*********************************/
internal fun Postcard.pretreatment(context: Context? = null): Postcard? {
    val pretreatmentService = ARouter.getInstance().navigation(PretreatmentService::class.java)
    if (null != pretreatmentService && !pretreatmentService.onPretreatment(context, this)) {
        // Pretreatment failed, navigation canceled.
        return null
    }
    try {
        LogisticsCenter.completion(this)
    } catch (e: Exception) {
        //这里弹toast 未找到router
        //回调call
        ARouter.getInstance().navigation(DegradeService::class.java)
            ?.apply { onLost(context, this@pretreatment) }
        return null
    }
    //不考虑实用拦截器
    //只考虑activity
    if (type != RouteType.ACTIVITY) return null
    return this
}

internal fun Postcard.buildIntent(activity: Activity): Intent {
    return Intent(activity, destination).apply {
        putExtras(this@buildIntent.extras)
        if (this@buildIntent.flags != -1)
            flags = this@buildIntent.flags
        if (!this@buildIntent.action.isNullOrEmpty())
            action = this@buildIntent.action
    }
}

fun Postcard.navigateActivity(activity: FragmentActivity) {
    pretreatment(activity)?.apply { activity.startActivity(buildIntent(activity)) }
}

fun Postcard.navigateForResult(
    activity: FragmentActivity, requestCode: Int,
    callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
) {
    pretreatment(activity)?.apply {
        NoResult.startActivityForResult(activity, buildIntent(activity), requestCode, callback)
    }
}

fun Postcard.navigateForResult(
    fragment: Fragment, requestCode: Int,
    callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
) {
    pretreatment(fragment.requireContext())?.apply {
        NoResult.startActivityForResult(fragment, buildIntent(fragment.requireActivity()), requestCode, callback)
    }
}
