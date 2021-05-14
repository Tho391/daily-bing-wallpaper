package com.thomas.apps.dailywallpaper.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.progressindicator.LinearProgressIndicator

@BindingAdapter("bindImage")
fun bindImage(imageView: ImageView, imageUrl: String?) {
    imageView.load(imageUrl)
}

@BindingAdapter("visibleIsGone")
fun showHide(view: View, isGone: Boolean) {
    view.visibility = if (isGone) View.GONE else View.VISIBLE
}

@BindingAdapter("progressShow")
fun bindProgress(progress: LinearProgressIndicator, show: Boolean) {
    if (show) progress.show() else progress.hide()
}