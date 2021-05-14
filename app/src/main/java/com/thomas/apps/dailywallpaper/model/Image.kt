package com.thomas.apps.dailywallpaper.model

import com.thomas.apps.dailywallpaper.adapter.ImageAdapter

data class Image(
    val startDate: String?,
    val fullStartDate: String?,
    val endDate: String?,
    val url: String?,
    val urlBase: String?,
    val copyRight: String?,
    val copyRightLink: String?,
    val title: String?,
    val quiz: String?,
    val id: Long = -1,
) {
    fun toImageItem(): ImageAdapter.ImageItem {
        return ImageAdapter.ImageItem(
            id = id.toString(),
            date = startDate ?: "",
            title = title ?: "",
            copyright = copyRight ?: "",
            imageUrl = url ?: ""
        )

    }
}