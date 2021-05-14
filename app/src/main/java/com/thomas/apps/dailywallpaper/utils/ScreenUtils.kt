package com.thomas.apps.dailywallpaper.utils

import android.content.Context


object ScreenUtils {
    fun Context.getScreenHeight() = resources.displayMetrics.heightPixels
    fun Context.getScreenWidth() = resources.displayMetrics.widthPixels
    fun Context.getStatusBarHeight(): Int {
        var result = 0
        val resourceId: Int =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}