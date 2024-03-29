package com.thomas.apps.dailywallpaper.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.thomas.apps.dailywallpaper.network.NetworkService
import com.thomas.apps.dailywallpaper.utils.WallpaperUtils.setWallpaper
import com.thomas.apps.dailywallpaper.worker.NotificationUtils.showNotification
import timber.log.Timber

class AutoWallpaperWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        val service = NetworkService.create(applicationContext)

        val response = service.requestWallpaper(0, 1)
        return if (response.isSuccessful) {
            val bingResponse = response.body()
            if (bingResponse == null) {
                applicationContext.showNotification("Api response is empty")

                Timber.i("Api response is empty")
                Result.failure(
                    workDataOf(
                        "ERROR" to "Api response is empty"
                    )
                )
            } else {
//                val url = bingResponse.images?.firstOrNull()?.url?.replace("1920x1080", "1080x1920")

                val url = bingResponse.images?.firstOrNull()?.toImage()?.url?.replace(
                    "1920x1080",
                    "1080x1920"
                )
                return if (url.isNullOrEmpty()) {
                    applicationContext.showNotification("Url is null")

                    Timber.i("Url is null")
                    Result.failure(
                        workDataOf(
                            "ERROR" to "Url is null"
                        )
                    )
                } else {
                    Timber.i("Set wallpaper")

                    applicationContext.setWallpaper(url)
                }
            }
        } else {
            applicationContext.showNotification(
                "Call api fail: ${
                    response.errorBody()?.string()
                }"
            )

            Timber.i(
                "Call api fail ${
                    response.errorBody()?.string()
                }"
            )
            Result.failure(
                workDataOf(
                    "ERROR" to "Call api fail ${
                        response.errorBody()?.string()
                    }"
                )
            )
        }

//        val imageUriInput =
//            inputData.getString(DownloadImageWork.IMAGE_URL) ?: return Result.failure()
//
//        val imageUrl = imageUriInput.replace("1920x1080", "1080x1920")
//
//
//
//        return applicationContext.setWallpaper(imageUrl)
    }
}