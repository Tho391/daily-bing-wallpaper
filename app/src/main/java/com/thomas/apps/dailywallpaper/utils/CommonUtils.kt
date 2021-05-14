package com.thomas.apps.dailywallpaper.utils

import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    private const val dateFormatPattern = "dd/MM/yyyy HH:mm:ss"
    private const val utcFormatPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    //time
    fun getCurrentTimeMillis() = System.currentTimeMillis()

    fun Long.toTime(): String {
        val dateFormat = SimpleDateFormat(dateFormatPattern, Locale.getDefault())
        val date = Date(this)
        return dateFormat.format(date)
    }

    inline fun <reified T> List<*>.asListOfType(): List<T>? =
        if (all { it is T })
            @Suppress("UNCHECKED_CAST")
            this as List<T> else
            null

    fun twoListHaveAtLeastOneItem(l1: List<String>, l2: List<String>): Boolean {
        val sum = l1 + l2
        val commonItem = sum.groupBy { it }.filter { it.value.size > 1 }.flatMap { it.value }
        return commonItem.isNotEmpty()
    }
}