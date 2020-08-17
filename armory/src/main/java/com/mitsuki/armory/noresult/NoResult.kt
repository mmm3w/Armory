package com.mitsuki.armory.noresult

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object NoResult {
    var tag = javaClass.simpleName

    private fun findOnResultFragment(activity: FragmentActivity): ResultFragment? =
            activity.supportFragmentManager.findFragmentByTag(tag) as? ResultFragment

    private fun getOnResultFragment(activity: FragmentActivity): ResultFragment {
        return findOnResultFragment(activity) ?: activity.supportFragmentManager.run {
            ResultFragment().apply {
                beginTransaction().add(this, NoResult.tag).commitAllowingStateLoss()
                executePendingTransactions()
            }
        }
    }

    fun startActivityForResult(
            fragment: Fragment, intent: Intent, requestCode: Int,
            callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit) {
        startActivityForResult(fragment.requireActivity(), intent, requestCode, callback)
    }

    fun startActivityForResult(
            activity: FragmentActivity, intent: Intent, requestCode: Int,
            callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit) {
        //最终都是通过当前的activity去查找隐藏的fragment
        //然后回调fragment的startActivityForResult
        //其中fragment的startActivityForResult的具体实现又会调用fragmentActivity的onStartActivityFromFragment
        //保证result回调不会出问题
        getOnResultFragment(activity).startActivityForResult(intent, requestCode, callback)
    }
}