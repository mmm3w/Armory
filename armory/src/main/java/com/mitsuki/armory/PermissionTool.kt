package com.mitsuki.armory

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


class PermissionTool(private val activityProvider: () -> ComponentActivity) {

    companion object {
        fun checkSelfPermission(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun checkSelfPermission(context: Context, permission: Array<String>): Boolean {
            for (item in permission) {
                if (ContextCompat.checkSelfPermission(context, item)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }
    }

    private val permissionLauncher =
        activityProvider().registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    private val multiplePermissionLauncher =
        activityProvider().registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

        }

    fun requestPermission(code: Int, permission: String, result: ((Boolean) -> Unit)? = null) {
        activityProvider.invoke().apply {
            if (checkSelfPermission(this, permission)) {
                result?.invoke(true)
            } else {
                //
                permissionLauncher.launch(permission)
            }
        }
    }

    fun requestPermission(
        code: Int,
        permission: Array<String>,
        result: ((List<String>, List<String>) -> Unit)? = null

    ) {
        activityProvider.invoke().apply {
            if (checkSelfPermission(this, permission)) {
                result?.invoke(permission.toList(), emptyList())
            } else {
                multiplePermissionLauncher.launch(permission)
            }
        }
    }
}

fun ComponentActivity.permissionTool() :PermissionTool = PermissionTool { this }