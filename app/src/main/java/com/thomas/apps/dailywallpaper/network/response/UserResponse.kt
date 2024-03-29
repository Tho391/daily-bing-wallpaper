package com.thomas.apps.dailywallpaper.network.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?
)