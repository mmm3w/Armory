package com.mitsuki.armorydemo.systemalert

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes

class AlertLayout @JvmOverloads constructor(
    context: Context, @LayoutRes layoutRes: Int, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mView = LayoutInflater.from(context).inflate(layoutRes, this)

    fun view(action: View.() -> Unit) {
        mView.apply(action)
    }
}