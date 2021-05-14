package com.thomas.apps.dailywallpaper.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
data class ImageEntity(
    val startDate: String?,
    val fullStartDate: String?,
    val endDate: String?,
    val url: String?,
    val urlBase: String?,
    val copyRight: String?,
    val copyRightLink: String?,
    val title: String?,
    val quiz: String?,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)
