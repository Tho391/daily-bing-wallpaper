package com.thomas.apps.dailywallpaper.utils

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.ListenableWorker
import androidx.work.workDataOf
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.thomas.apps.dailywallpaper.worker.DownloadImageWork
import com.thomas.apps.dailywallpaper.worker.NotificationUtils.showNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WallpaperUtils {
    suspend fun Context.setWallpaper(imageUrl: String): ListenableWorker.Result {
        val context = this

        return withContext(Dispatchers.IO) {
            val loader = ImageLoader(context)
            val req = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = loader.execute(req)

            return@withContext when (result) {
                is SuccessResult -> {
                    val bitmap = result.drawable.toBitmap()

                    val wallpaperManager = WallpaperManager.getInstance(context)
                    try {
                        if (ContextCompat.checkSelfPermission(
                                context,
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

                            ListenableWorker.Result.success()
                        } else {

                            applicationContext.showNotification("Apply wallpaper fail")

                            ListenableWorker.Result.failure(
                                workDataOf(
                                    DownloadImageWork.ERROR to "Require permission"
                                )
                            )
                        }

                    } catch (e: Exception) {
                        applicationContext.showNotification("Apply wallpaper success")

                        ListenableWorker.Result.failure(
                            workDataOf(
                                DownloadImageWork.ERROR to "Image error: ${e.message}"
                            )
                        )
                    }
                }
                else -> {
                    applicationContext.showNotification("Download image error")

                    ListenableWorker.Result.failure(
                        workDataOf(
                            DownloadImageWork.ERROR to "Download image error"
                        )
                    )
                }
            }
        }
    }
}