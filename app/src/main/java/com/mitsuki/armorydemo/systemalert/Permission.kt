package com.mitsuki.armorydemo.systemalert

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

inline fun Context.overlayPermission(after: (Intent?) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (Settings.canDrawOverlays(this)) {
            after(null)
        } else {
            after(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:$packageName")
            })
        }
    } else {
        after(null)
    }
}
