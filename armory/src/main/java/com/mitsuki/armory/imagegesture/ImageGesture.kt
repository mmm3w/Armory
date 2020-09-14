package com.mitsuki.armory.imagegesture

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import kotlin.math.pow
import kotlin.math.sqrt

@SuppressLint("ClickableViewAccessibility")
class ImageGesture(private val mImageView: ImageView) : View.OnTouchListener,
    View.OnLayoutChangeListener {

    var startType = StartType.AUTO_LEFT
        set(startType) {
            field = startType
            initBase()
        }

    private val mDrawMatrix = Matrix()
    private val mBaseMatrix = Matrix()
    private val mDecoMatrix = Matrix()
    private val mDisplayRect = RectF()
    private val mMatrixValues = FloatArray(9)

    //启用mSlideType阈值，除非超过阈值，否则都为NONE mode
    private var mScaleThreshold = 0.0f

    private val mScaleGestureDetector = ScaleGestureDetector(
        mImageView.context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                return detector?.run {
                    var handled = false
                    val currentScale = mDecoMatrix.getScale()
                    if (scaleFactor > 1f && currentScale < MAX_SCALE) {
                        if (currentScale * scaleFactor > MAX_SCALE) {
                            mDecoMatrix.postScale(
                                MAX_SCALE / currentScale, MAX_SCALE / currentScale,
                                focusX, focusY
                            )
                        } else {
                            mDecoMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
                        }
                        updateImageMatrix()
                        handled = true
                    }

                    if (scaleFactor < 1f && currentScale > 1f) {
                        if (currentScale * scaleFactor < 1f) {
                            mDecoMatrix.postScale(
                                1f / currentScale, 1f / currentScale, focusX, focusY
                            )
                        } else {
                            mDecoMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
                        }
                        updateImageMatrix()
                        handled = true
                    }
                    handled
                } ?: false
            }
        })
    private val mGestureDetector =
        GestureDetector(mImageView.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                var handled = false
                val viewWidth = mImageView.width
                val viewHeight = mImageView.height
                finalMatrix.getDisplayRect()
                if (distanceX > 0) {
                    if (mDisplayRect.right > viewWidth) handled = true
                } else {
                    if (mDisplayRect.left < 0f) handled = true
                }
                if (distanceY > 0) {
                    if (mDisplayRect.bottom > viewHeight) handled = true
                } else {
                    if (mDisplayRect.top < 0f) handled = true
                }
                if (handled) {
                    mDecoMatrix.postTranslate(-distanceX, -distanceY)
                    updateImageMatrix()
                }
                return handled
            }

            override fun onFling(
                e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float
            ): Boolean {
                Log.e("asdf","onFling velocityX $velocityX | velocityY $velocityY")
                return super.onFling(e1, e2, velocityX, velocityY)
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                val currentScale = mDecoMatrix.getScale()
                if (currentScale > 1f) {
                    mDecoMatrix.postScale(1f / currentScale, 1f / currentScale)
                } else {
                    mDecoMatrix.postScale(MAX_SCALE / currentScale, MAX_SCALE / currentScale)
                }
                updateImageMatrix()
                return true
            }
        })

    companion object {
        const val MAX_SCALE = 3.0f
    }

    init {
        mImageView.scaleType = ImageView.ScaleType.MATRIX
        mImageView.setOnTouchListener(this)
        mImageView.addOnLayoutChangeListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        var handled = false
        if (mScaleGestureDetector.onTouchEvent(event)) {
            handled = true
        }
        if (mGestureDetector.onTouchEvent(event)) {
            handled = true
        }
        return handled
    }

    override fun onLayoutChange(
        v: View?, left: Int, top: Int, right: Int, bottom: Int,
        oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
    ) {
        initBase()
    }

    private fun initBase() {
        val drawable = mImageView.drawable ?: return
        val viewWidth = mImageView.width  //view的宽度
        val viewHeight = mImageView.height  //view的高度
        val sourceWidth = drawable.intrinsicWidth  //源图宽度
        val sourceHeight = drawable.intrinsicHeight  //源图高度
        mBaseMatrix.reset()  //初始化矩阵
        val widthRatio = viewWidth.toFloat() / sourceWidth.toFloat()  //宽度比例
        val heightRatio = viewHeight.toFloat() / sourceHeight.toFloat()  //高度比例

        val scale: Float
        val translateX: Float
        when (startType) {
            StartType.NONE -> {
                scale = if (widthRatio < heightRatio) widthRatio else heightRatio
                translateX = (viewWidth - sourceWidth * scale) / 2
            }
            StartType.TOP -> {
                scale = when {
                    widthRatio > mScaleThreshold -> widthRatio
                    widthRatio < heightRatio -> widthRatio
                    else -> heightRatio
                }
                translateX = (viewWidth - sourceWidth * scale) / 2
            }
            StartType.LEFT -> {
                scale = when {
                    heightRatio > mScaleThreshold -> heightRatio
                    widthRatio < heightRatio -> widthRatio
                    else -> heightRatio
                }
                translateX = ((viewWidth - sourceWidth * scale) / 2).coerceAtLeast(0f)
            }
            StartType.RIGHT -> {
                scale = when {
                    heightRatio > mScaleThreshold -> heightRatio
                    widthRatio < heightRatio -> widthRatio
                    else -> heightRatio
                }
                val temp = (viewWidth - sourceWidth * scale) / 2
                translateX = if (temp < 0) temp * 2 else temp
            }
            StartType.AUTO_LEFT -> {
                val tempScale = if (widthRatio < heightRatio) heightRatio else widthRatio
                scale = when {
                    tempScale > mScaleThreshold -> tempScale
                    widthRatio < heightRatio -> widthRatio
                    else -> heightRatio
                }
                translateX = ((viewWidth - sourceWidth * scale) / 2).coerceAtLeast(0f)
            }
            StartType.AUTO_RIGHT -> {
                val tempScale = if (widthRatio < heightRatio) heightRatio else widthRatio
                if (tempScale > mScaleThreshold) {
                    scale = tempScale
                    translateX =
                        if (tempScale == widthRatio) 0f else viewWidth - sourceWidth * scale
                } else {
                    scale = if (widthRatio < heightRatio) widthRatio else heightRatio
                    translateX = (viewWidth - sourceWidth * scale) / 2
                }
            }
        }
        val translateY: Float = ((viewHeight - sourceHeight * scale) / 2).coerceAtLeast(0f)

        mBaseMatrix.postScale(scale, scale)
        mBaseMatrix.postTranslate(translateX, translateY)
        mImageView.imageMatrix = finalMatrix
    }

    fun updateImageMatrix() {
        checkDecoMatrix()
        mImageView.imageMatrix = finalMatrix
    }

    private fun checkDecoMatrix() {
        finalMatrix.getDisplayRect()
        val viewWidth = mImageView.width  //view的宽度
        val viewHeight = mImageView.height  //view的高度
        var offsetX = 0f
        var offsetY = 0f

        if (mDisplayRect.width() > viewWidth) {
            if (mDisplayRect.left > 0) {
                offsetX = -mDisplayRect.left
            }
            if (mDisplayRect.right < viewWidth) {
                offsetX = viewWidth - mDisplayRect.right
            }
        } else {
            val correctLeft = (viewWidth - mDisplayRect.width()) / 2
            offsetX = correctLeft - mDisplayRect.left
        }

        if (mDisplayRect.height() > viewHeight) {
            if (mDisplayRect.top > 0) {
                offsetY = -mDisplayRect.top
            }
            if (mDisplayRect.bottom < viewHeight) {
                offsetY = viewHeight - mDisplayRect.bottom
            }
        } else {
            val correctTop = (viewHeight - mDisplayRect.height()) / 2
            offsetY = correctTop - mDisplayRect.top
        }

        mDecoMatrix.postTranslate(offsetX, offsetY)
    }

    private fun Matrix.getDisplayRect() {
        val drawable: Drawable = mImageView.drawable ?: return
        mDisplayRect.set(
            0f,
            0f,
            drawable.intrinsicWidth.toFloat(),
            drawable.intrinsicHeight.toFloat()
        )
        mapRect(mDisplayRect)
    }

    fun Matrix.getScale(): Float {
        return sqrt(
            getValue(Matrix.MSCALE_X).toDouble().pow(2.0)
                    + getValue(Matrix.MSKEW_Y).toDouble().pow(2.0)
        ).toFloat()
    }

    private fun Matrix.getValue(key: Int): Float {
        getValues(mMatrixValues)
        return mMatrixValues[key]
    }

    private val finalMatrix: Matrix
        get() = mDrawMatrix.apply {
            set(mBaseMatrix)
            postConcat(mDecoMatrix)
        }

}