package com.mitsuki.armory.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.mitsuki.armory.R
import com.mitsuki.armory.dp2px

class RatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var baseSize = dp2px(16f)
        set(value) {
            field = value
            borderDrawable?.setBounds(0, 0, value, value)
            halfDrawable?.setBounds(0, 0, value, value)
            solidDrawable?.setBounds(0, 0, value, value)
        }

    var intervalPadding = dp2px(2f)
    var maxRating = 5
    var rating = -1f

    var borderDrawable = obtainDrawable(R.drawable.ic_baseline_star_border_16)
        set(value) {
            field = value
            field?.setBounds(0, 0, baseSize, baseSize)
        }
    var halfDrawable = obtainDrawable(R.drawable.ic_baseline_star_half_16)
        set(value) {
            field = value
            field?.setBounds(0, 0, baseSize, baseSize)
        }
    var solidDrawable = obtainDrawable(R.drawable.ic_baseline_star_16)
        set(value) {
            field = value
            field?.setBounds(0, 0, baseSize, baseSize)
        }

    init {
        borderDrawable!!.setBounds(0, 0, baseSize, baseSize)
        halfDrawable!!.setBounds(0, 0, baseSize, baseSize)
        solidDrawable!!.setBounds(0, 0, baseSize, baseSize)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            else -> baseSize * maxRating + (maxRating - 1) * intervalPadding + paddingLeft + paddingRight
        }

        var height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            else -> baseSize + paddingTop + paddingBottom
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        val saved = canvas?.save()
        canvas?.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        for (i in 0 until maxRating) {
            if (rating < 0) {
                borderDrawable?.draw(canvas!!)
                canvas?.translate((baseSize + intervalPadding).toFloat(), 0f)
                continue
            }

            (rating - i).let {
                if (it >= 0.75) {
                    solidDrawable?.draw(canvas!!)
                    canvas?.translate((baseSize + intervalPadding).toFloat(), 0f)
                } else if (it < 0.75 && it >= 0.25) {
                    halfDrawable?.draw(canvas!!)
                    canvas?.translate((baseSize + intervalPadding).toFloat(), 0f)
                } else {
                    borderDrawable?.draw(canvas!!)
                    canvas?.translate((baseSize + intervalPadding).toFloat(), 0f)
                }
            }
        }
        canvas?.restoreToCount(saved!!)
    }

    fun setDrawable(
        @DrawableRes solid: Int = R.drawable.ic_baseline_star_16,
        @DrawableRes half: Int = R.drawable.ic_baseline_star_half_16,
        @DrawableRes border: Int = R.drawable.ic_baseline_star_border_16
    ) {
        this.solidDrawable = obtainDrawable(solid)
        this.halfDrawable = obtainDrawable(half)
        this.borderDrawable = obtainDrawable(border)
    }

    private fun obtainDrawable(@DrawableRes id: Int): Drawable? =
        ResourcesCompat.getDrawable(resources, id, null)?.apply {
            setBounds(0, 0, baseSize, baseSize)
        }
}