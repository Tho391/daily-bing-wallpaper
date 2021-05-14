package com.thomas.apps.dailywallpaper.utils

import android.content.Context
import android.util.TypedValue
import com.thomas.apps.dailywallpaper.R

object ResourceUtils {
    fun Context.getColorOnBackground(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorOnBackground, type, true)
        return type.data
    }

    fun Context.getColorBackground(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.backgroundColor, type, true)
        return type.data
    }

    fun Context.getColorPrimary(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, type, true)
        return type.data
    }

    fun Context.getColorSecondary(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorSecondary, type, true)
        return type.data
    }

    fun Context.getColorOnError(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorError, type, true)
        return type.data
    }
}