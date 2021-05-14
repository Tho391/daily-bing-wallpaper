package com.thomas.apps.dailywallpaper.network.response.bing


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.thomas.apps.dailywallpaper.model.Image

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "startdate") val startdate: String?,
    @Json(name = "fullstartdate") val fullstartdate: String?,
    @Json(name = "enddate") val enddate: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "urlbase") val urlbase: String?,
    @Json(name = "copyright") val copyright: String?,
    @Json(name = "copyrightlink") val copyrightlink: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "quiz") val quiz: String?,
    @Json(name = "wp") val wp: Boolean?,
    @Json(name = "hsh") val hsh: String?,
    @Json(name = "drk") val drk: Int?,
    @Json(name = "top") val top: Int?,
    @Json(name = "bot") val bot: Int?,
    @Json(name = "hs") val hs: List<Any>?
) {
    fun toImage(): Image {
        return Image(
            startDate = startdate,
            fullStartDate = fullstartdate,
            endDate = enddate,
            url = url,
            urlBase = urlbase,
            copyRight = copyright,
            copyRightLink = copyrightlink,
            title = title,
            quiz = quiz
        )
    }
}