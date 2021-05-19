package com.thomas.apps.dailywallpaper.worker

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import coil.request.ImageResult
import coil.request.SuccessResult
import com.thomas.apps.dailywallpaper.utils.ScreenUtils.getScreenHeight
import com.thomas.apps.dailywallpaper.utils.ScreenUtils.getScreenWidth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.io.IOException
import java.lang.Exception
import java.util.*

class DownloadImageWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        const val IMAGE_URL = "IMAGE_URL"
        const val ERROR = "ERROR"
    }

    override suspend fun doWork(): Result {

        val imageUriInput = inputData.getString(IMAGE_URL) ?: return Result.failure()

        val with = applicationContext.getScreenWidth()
        val height = applicationContext.getScreenHeight()
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
        val mimeType = "image/png"
        val displayName = UUID.randomUUID().toString() + ".png"

        return try {
            saveBitmap(applicationContext,bm,format,mimeType, displayName)

            null
        }catch (e: Exception){
            e.message
        }
    }

    @Throws(IOException::class)
    fun saveBitmap(
        context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat,
        mimeType: String, displayName: String
    ): Uri {

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }
        }

        var uri: Uri? = null

        return runCatching {
            with(context.contentResolver) {
                insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also {
                    uri = it // Keep uri reference so it can be removed on failure

                    openOutputStream(it)?.use { stream ->
                        if (!bitmap.compress(format, 95, stream))
                            throw IOException("Failed to save bitmap.")
                    } ?: throw IOException("Failed to open output stream.")

                } ?: throw IOException("Failed to create new MediaStore record.")
            }
        }.getOrElse {
            uri?.let { orphanUri ->
                // Don't leave an orphan entry in the MediaStore
                context.contentResolver.delete(orphanUri, null, null)
            }

            throw it
        }
    }
}