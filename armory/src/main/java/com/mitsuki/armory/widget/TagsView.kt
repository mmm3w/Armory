package com.mitsuki.armory.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

class TagsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (childCount < 1) return

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)


        //先测量第一个作为组名的view
        getChildAt(0).let {
            measureChild(it, widthMeasureSpec, heightMeasureSpec)

        }

        //当布局模式为wrap_content的时候
        //需要确定宽度最宽的view来确定viewgroup的width
        var maxWidth = 0






        for (i in 1 until childCount) {
            getChildAt(i).let {
                measureChild(it, widthMeasureSpec, heightMeasureSpec)

                maxWidth = Math.max(maxWidth,it.measuredWidth)

                it.measuredHeight
                it.measuredWidth
            }

        }


    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("Not yet implemented")
    }
}