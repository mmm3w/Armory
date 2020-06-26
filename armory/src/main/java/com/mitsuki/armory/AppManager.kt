package com.mitsuki.armory

import android.app.Activity
import java.lang.ref.WeakReference

class AppManager private constructor() {
    companion object {
        val INSTANCE: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { AppManager() }
    }

    private val mActivityList: MutableList<WeakReference<Activity?>> = ArrayList()

    fun addActivity(activity: Activity) {
        mActivityList.add(WeakReference(activity))
    }

    fun removeActivity(activity: Activity) {
        for (item in mActivityList) {
            if (item.get() == activity) {
                mActivityList.remove(item)
                break
            }
        }
    }

    fun finishAll() {
        for (item in mActivityList) {
            item.get()?.finish()
        }
    }
}