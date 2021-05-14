package com.thomas.apps.dailywallpaper.network.param


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserParam(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?
)