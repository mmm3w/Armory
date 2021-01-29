package com.mitsuki.armorydemo

import android.app.Application
import com.mitsuki.armorydemo.systemalert.AlertManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AlertManager.init(this)
    }
}