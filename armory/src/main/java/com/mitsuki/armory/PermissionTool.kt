package com.mitsuki.armory

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionTool {


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