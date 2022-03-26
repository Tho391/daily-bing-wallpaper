package com.thomas.apps.dailywallpaper.network.response.bing


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BingResponse(
    @Json(name = "images") val images: List<Image>?,
    @Json(name = "tooltips") val tooltips: Tooltips?
)