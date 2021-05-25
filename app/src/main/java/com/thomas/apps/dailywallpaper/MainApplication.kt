package com.thomas.apps.dailywallpaper

import android.R.attr.delay
import android.app.Application
import android.os.Build
import androidx.work.*
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import com.thomas.apps.dailywallpaper.worker.AutoWallpaperWorker
import com.thomas.apps.dailywallpaper.worker.NotificationUtils.createNotificationChannel
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


class MainApplication : Application(),Configuration.Provider {

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.VERBOSE)
            .build()

    override fun onCreate() {
        super.onCreate()

        initTimber()

        setUpCoil()

        applicationContext.createNotificationChannel()

        startAutoWallpaperWorker()
    }

    private fun startAutoWallpaperWorker() {
        val calendar15h15 = Calendar.getInstance(Locale.getDefault())
        calendar15h15.set(Calendar.HOUR_OF_DAY, 15)
        calendar15h15.set(Calendar.MINUTE, 15)


        val now = Calendar.getInstance(Locale.getDefault())

        val diff = calendar15h15.timeInMillis - now.timeInMillis
        val delayMillis = if (diff < 0) 0 else diff


        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            //.setRequiresCharging(true)
            .build()

        val setWallpaperWorkRequest =
            PeriodicWorkRequestBuilder<AutoWallpaperWorker>(1, TimeUnit.DAYS)
                .addTag("auto wallpaper")
                .setConstraints(constraints)
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "auto wallpaper work",
                ExistingPeriodicWorkPolicy.KEEP,
                setWallpaperWorkRequest
            )
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setUpCoil() {
        val imageLoader = ImageLoader.Builder(applicationContext)
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder())
                } else {
                    add(GifDecoder())
                }
                add(SvgDecoder(applicationContext))

            }
            .build()
        Coil.setImageLoader(imageLoader)
    }
}