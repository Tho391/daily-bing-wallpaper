package com.thomas.apps.dailywallpaper.network.response.bing


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.thomas.apps.dailywallpaper.model.Image
import com.thomas.apps.dailywallpaper.network.EndPoints
import com.thomas.apps.dailywallpaper.network.NetworkService

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "startdate") val startDate: String?,
    @Json(name = "fullstartdate") val fullStartDate: String?,
    @Json(name = "enddate") val endDate: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "urlbase") val urlBase: String?,
    @Json(name = "copyright") val copyright: String?,
    @Json(name = "copyrightlink") val copyrightLink: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "quiz") val quiz: String?,
    @Json(name = "wp") val wp: Boolean?,
    @Json(name = "hsh") val hsh: String?,
    @Json(name = "drk") val drk: Int?,
    @Json(name = "top") val top: Int?,
    @Json(name = "bot") val bot: Int?,
    @Json(name = "hs") val hs: List<Any>?
) {
    val host = EndPoints.BING_HOST

    fun getUrlBasePath(): String {
        return host + urlBase
    }

    fun getUrlPath(): String {
        return host + url
    }

    fun getCopyrightLinkPath(): String {
        return host + copyrightLink
    }

    fun toImage(): Image {
        return Image(
            startDate = startDate,
            fullStartDate = fullStartDate,
            endDate = endDate,
            url = getUrlPath(),
            urlBase = getUrlBasePath(),
            copyRight = copyright,
            copyRightLink = getCopyrightLinkPath(),
            title = title,
            quiz = quiz
        )
    }
}