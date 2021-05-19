package com.thomas.apps.dailywallpaper.model

import com.thomas.apps.dailywallpaper.adapter.ImageAdapter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

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

    fun getNexKey(): Int? {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        try {
            val date = dateFormat.parse(startDate ?: "")?.time
            val now = Date().time

            if (date == null) {
                return null
            } else {
                val diff: Long = abs(date - now)
                val seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val days = hours / 24

                val page = (days - 1) / 8 + 1 + 1
                return page.toInt()
            }
        } catch (e: Exception) {
            Timber.e(e)

            return null
        }

    }
}