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

        //在EXACTLY模式下直接确定大小
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            setMeasuredDimension(widthSize, heightSize)
            return
        }

        //先测定第一个view
        val firstView = getChildAt(0).also {
            measureChild(it, widthMeasureSpec, heightMeasureSpec)
        }

        //除去第一个view，其他view尺寸限制范围
        val limitedWidth = when (widthMode) {
            MeasureSpec.UNSPECIFIED -> -1
            else -> (widthSize - firstView.measuredWidth).coerceAtLeast(0)
        }
        val limitedHeight = when (heightMode) {
            MeasureSpec.UNSPECIFIED -> -1
            else -> (heightSize - firstView.measuredHeight).coerceAtLeast(0)
        }

        var tempWidth = 0
        var tempHeight = 0
        var currentWidth = 0
        var currentHeight = 0

        //TODO:改造measureChild中的spec参数，其中建议size需要重新给定
        for (i in 1 until childCount) {
            getChildAt(i).run {
                measureChild(this, widthMeasureSpec, heightMeasureSpec)
                when (widthMode) {
                    MeasureSpec.EXACTLY -> {
                        //tempHeight用于存储当前行最大的view的高度，是作为一个预计累加的高度

                        //累加宽度值
                        tempWidth += measuredWidth
                        //判断累加值有没有超过限定的宽度
                        if (tempWidth > limitedWidth) {
                            //该view属于下一行
                            //重置临时参数
                            tempWidth = 0  //用于保证下次累加判断
                            tempHeight = 0  //因为换行了，所以当前行的最高高度清零
                        } else {
                            //该view属于本行
                            //先减去预先增加的高度
                            //因为temp初始值为0,表示首次到此处是预加高度为0
                            currentHeight -= tempHeight
                        }

                        //拿到当前行需要预加的最大高度
                        tempHeight = tempHeight.coerceAtLeast(measuredHeight)
                        //加上预计高度
                        currentHeight += tempHeight
                    }
                    MeasureSpec.AT_MOST -> {
                        //获取最宽的view
                        currentWidth = currentWidth.coerceAtLeast(measuredWidth)
                            .coerceAtMost(limitedWidth)

                        //判断空余是否够塞下view
                        if (measuredWidth > tempWidth) {
                            //view去下一行
                            tempWidth = currentWidth
                            tempHeight = tempHeight.coerceAtLeast(measuredHeight)
                            currentHeight += tempHeight
                            tempHeight = 0
                        } else {
                            //view留在本行
                            tempWidth -= measuredWidth
                            currentHeight -= tempHeight
                            tempHeight = tempHeight.coerceAtLeast(measuredHeight)
                            currentHeight += tempHeight
                        }
                    }
                    MeasureSpec.UNSPECIFIED -> {
                        currentWidth += measuredWidth
                        currentHeight = currentHeight.coerceAtLeast(measuredHeight)
                    }
                }
            }
        }

        val resultWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> widthSize.coerceAtMost(currentWidth + firstView.measuredWidth)
            else -> currentWidth + firstView.measuredWidth
        }

        val resultHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> heightSize.coerceAtMost(currentHeight)
                .coerceAtMost(limitedHeight)
            else -> currentHeight
        }

        setMeasuredDimension(resultWidth, resultHeight)

    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount < 1) return

        val firstChild = getChildAt(0)
        firstChild.layout(l, t, l + firstChild.measuredWidth, t + firstChild.measuredHeight)

        val startLeft = l + firstChild.measuredWidth
        val maxWidth = width
        var leftOffset = 0
        var topOffset = t

        for (i in 1 until childCount) {
            getChildAt(i).run {
                if (startLeft + leftOffset + measuredWidth > maxWidth) {
                    leftOffset = 0
                    topOffset += measuredHeight
                }

                layout(
                    startLeft + leftOffset,
                    topOffset,
                    startLeft + leftOffset + measuredWidth,
                    topOffset + measuredHeight
                )
                leftOffset += measuredWidth
            }
        }

    }
}