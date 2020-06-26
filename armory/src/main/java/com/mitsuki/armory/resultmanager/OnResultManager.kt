package com.mitsuki.utilspack.utils.resultmanager

import android.app.Activity
import android.content.Intent
import android.service.carrier.CarrierMessagingService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object OnResultManager {
    var tag = javaClass.simpleName

    private fun findOnResultFragment(activity: androidx.fragment.app.FragmentActivity): ResultFragment? =
        activity.supportFragmentManager.findFragmentByTag(tag)?.let { it as ResultFragment }


    private fun getOnResultFragment(activity: androidx.fragment.app.FragmentActivity): ResultFragment {
        findOnResultFragment(activity)?.let {
            return it
        }

        val fragment = ResultFragment()
        activity.supportFragmentManager
            .beginTransaction()
            .add(fragment, tag)
            .commitAllowingStateLoss()
        activity.supportFragmentManager
            .executePendingTransactions()
        return fragment
    }

    fun startActivityForResult(
        fragment: androidx.fragment.app.Fragment,
        intent: Intent,
        requestCode: Int,
        callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
    ) {
        this.startActivityForResult((fragment.activity ?: return), intent, requestCode, callback)
    }

    fun startActivityForResult(
        activity: androidx.fragment.app.FragmentActivity,
        intent: Intent,
        requestCode: Int,
        callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
    ) {
        getOnResultFragment(activity).startActivityForResult(intent, requestCode, callback)
    }


    fun requestPermissions(
        fragment: androidx.fragment.app.Fragment,
        permissions: Array<out String>,
        requestCode: Int,
        callback: (requestCode: Int, permissions: Array<String>, grantResults: IntArray) -> Unit
    ) {
        this.requestPermissions((fragment.activity ?: return), permissions, requestCode, callback)
    }

    fun requestPermissions(
        activity: androidx.fragment.app.FragmentActivity,
        permissions: Array<out String>,
        requestCode: Int,
        callback: (requestCode: Int, permissions: Array<String>, grantResults: IntArray) -> Unit
    ) {
        getOnResultFragment(activity).requestPermissions(permissions, requestCode, callback)
    }


}