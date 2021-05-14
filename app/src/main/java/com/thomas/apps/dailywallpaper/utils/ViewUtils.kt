package com.thomas.apps.dailywallpaper.utils

import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.roundToInt

object ViewUtils {
    fun dpToPx(dp: Float): Int {
        val density: Float = Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }

    fun pxToDp(px: Float): Float {
        val densityDpi: Int = Resources.getSystem().displayMetrics.densityDpi
        return px / (densityDpi / 160f)
    }

    fun View.viewGone(gone: Boolean) {
        this.visibility = if (gone) View.GONE else View.VISIBLE
    }

    fun View.viewGone() {
        this.visibility = View.GONE
    }

    fun View.viewVisible() {
        this.visibility = View.VISIBLE
    }

    fun View.viewInvisible() {
        this.visibility = View.INVISIBLE
    }

    fun View.enable() {
        this.isEnabled = true
    }

    fun View.disable() {
        this.isEnabled = false
    }

    fun View.elevationHigh() {
        this.elevation = 1F
    }

    fun View.elevationLow() {
        this.elevation = 0F
    }

    fun TextView.clearText() {
        text = ""
    }

    fun TextInputLayout.markRequired() {
        if (hint?.endsWith(" *") == false)
            hint = "$hint *"
    }

    fun TextInputLayout.markRequiredInRed() {
        if (hint?.endsWith(" *") == false)
            hint = buildSpannedString {
                append(hint)
                color(Color.RED) { append(" *") } // Mind the space prefix.
            }
    }
}