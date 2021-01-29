package com.mitsuki.armorydemo.systemalert

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager

object AlertManager {

    private lateinit var mWindowManager: WindowManager
    private lateinit var mScreenSize: Point

    fun init(context: Context) {
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mScreenSize = Point().apply { mWindowManager.defaultDisplay.getSize(this) }
    }


    fun addView(view: View) {
        val params = WindowManager.LayoutParams().apply {
            type = windowType()
            format = PixelFormat.RGBA_8888
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            gravity = Gravity.START or Gravity.TOP
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            x = 0
            y = mScreenSize.y / 2
        }
        view.layoutParams = params
        mWindowManager.addView(view, params)
    }


    private fun windowType(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
    }

}