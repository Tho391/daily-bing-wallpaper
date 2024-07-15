package com.thomas.apps.dailywallpaper.worker

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.graphics.drawable.toBitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.thomas.apps.dailywallpaper.worker.NotificationUtils.showNotificationDownload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class DownloadImageWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        const val IMAGE_URL = "IMAGE_URL"
        const val ERROR = "ERROR"
    }

    override suspend fun doWork(): Result {

        val imageUriInput = inputData.getString(IMAGE_URL) ?: return Result.failure()

        val imageUrl = imageUriInput.replace("1920x1080", "1080x1920")
        return downloadFile(imageUrl)

    }

    private suspend fun downloadFile(imageUriInput: String): Result {
        return withContext(Dispatchers.IO) {
            val loader = ImageLoader(applicationContext)
            val req = ImageRequest.Builder(applicationContext)
                .data(imageUriInput)
                .allowHardware(false)
                .build()

            val result = loader.execute(req)

            return@withContext when (result) {
                is SuccessResult -> {
                    val drawable = result.drawable
                    val error = saveDrawable(drawable)

                    if (error == null) {
                        Result.success()
                    } else {
                        Result.failure(
                            workDataOf(
                                ERROR to "Save image error: $error"
                            )
                        )
                    }
                }

                else -> {
                    Result.failure(
                        workDataOf(
                            ERROR to "Download image error"
                        )
                    )
                }
            }
        }
    }

    private fun saveDrawable(drawable: Drawable): String? {
        val bm = drawable.toBitmap()

        val format = Bitmap.CompressFormat.PNG
        val displayName = UUID.randomUUID().toString() + ".png"

        return try {
            saveBitmapToCache(applicationContext, bm, format, displayName)

            null
        } catch (e: Exception) {
            e.message
        }
    }

    @Throws(IOException::class)
    fun saveBitmapToCache(
        context: Context,
        bitmap: Bitmap,
        format: Bitmap.CompressFormat,
        fileName: String
    ): Uri {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, fileName)

        return runCatching {
            FileOutputStream(file).use { outputStream ->
                if (!bitmap.compress(format, 95, outputStream)) {
                    throw IOException("Failed to save bitmap to cache.")
                }
            }
            Uri.fromFile(file)
        }.getOrElse {
            // Handle exceptions, e.g., log the error
            throw it
        }
    }
}