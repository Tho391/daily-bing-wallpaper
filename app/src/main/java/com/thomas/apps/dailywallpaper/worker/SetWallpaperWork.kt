package com.thomas.apps.dailywallpaper.worker

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.thomas.apps.dailywallpaper.worker.NotificationUtils.showNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetWallpaperWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val imageUriInput =
            inputData.getString(DownloadImageWork.IMAGE_URL) ?: return Result.failure()

        val imageUrl = imageUriInput.replace("1920x1080", "1080x1920")

        return setWallpaper(imageUrl)

    }

    private suspend fun setWallpaper(imageUrl: String): Result {
        return withContext(Dispatchers.IO) {
            val loader = ImageLoader(applicationContext)
            val req = ImageRequest.Builder(applicationContext)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = loader.execute(req)

            return@withContext when (result) {
                is SuccessResult -> {
                    val bitmap = result.drawable.toBitmap()

                    val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                    try {
                        if (ContextCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.SET_WALLPAPER
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
//                            wallpaperManager.setBitmap(bitmap)
                            wallpaperManager.setBitmap(
                                bitmap,
                                null,
                                false,
                                WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
                            )

                            applicationContext.showNotification("Apply wallpaper success")

                            Result.success()
                        } else {

                            applicationContext.showNotification("Apply wallpaper fail")

                            Result.failure(
                                workDataOf(
                                    DownloadImageWork.ERROR to "Require permission"
                                )
                            )
                        }

                    } catch (e: Exception) {
                        applicationContext.showNotification("Apply wallpaper success")

                        Result.failure(
                            workDataOf(
                                DownloadImageWork.ERROR to "Image error: ${e.message}"
                            )
                        )
                    }
                }
                else -> {
                    applicationContext.showNotification("Download image error")

                    Result.failure(
                        workDataOf(
                            DownloadImageWork.ERROR to "Download image error"
                        )
                    )
                }
            }
        }
    }
}