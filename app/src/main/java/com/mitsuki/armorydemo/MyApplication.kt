package com.mitsuki.armorydemo

import android.app.Application
import coil.util.CoilUtils
import com.mitsuki.armory.httprookie.HttpRookie
import com.mitsuki.loadprogress.ProgressProvider
import com.mitsuki.systemoverlay.OverlayManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        HttpRookie.configOkHttp = {
            addInterceptor(ProgressProvider.imageLoadInterceptor)
        }
    }

}