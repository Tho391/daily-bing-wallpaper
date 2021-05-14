package com.thomas.apps.dailywallpaper.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    val serverId: String,
    val username: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)