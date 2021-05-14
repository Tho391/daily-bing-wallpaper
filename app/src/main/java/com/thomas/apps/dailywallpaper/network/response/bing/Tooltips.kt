package com.thomas.apps.dailywallpaper.network.response.bing


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tooltips(
    @Json(name = "loading") val loading: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "next") val next: String?,
    @Json(name = "walle") val walle: String?,
    @Json(name = "walls") val walls: String?
)