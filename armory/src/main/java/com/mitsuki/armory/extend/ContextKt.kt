package com.mitsuki.armory.extend

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.fragment.app.Fragment

/** toast *****************************************************************************************/
fun Context.toast(value: String) =
    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()

fun Context.toastLong(value: String) =
    Toast.makeText(this, value, Toast.LENGTH_LONG).show()

fun Fragment.toast(value: String) = context?.toast(value)

fun Fragment.toastLong(value: String) = context?.toastLong(value)

/** soft input ************************************************************************************/
fun Activity.showSoftInput() =
    currentFocus?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(it, 0)
    } ?: false

fun Activity.hideSoftInput() =
    currentFocus?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(it.windowToken, 0)
    } ?: false

fun Fragment.showSoftInput() = requireActivity().showSoftInput()

fun Fragment.hideSoftInput() = requireActivity().hideSoftInput()

/**************************************************************************************************/

@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

