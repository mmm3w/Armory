package com.mitsuki.armorydemo

import android.app.Application
import com.mitsuki.armory.inputmeasure.InputHeight

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        InputHeight.init(this)
    }

}