package com.thomas.apps.dailywallpaper

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import com.thomas.apps.dailywallpaper.worker.AutoWallpaperWorker
import com.thomas.apps.dailywallpaper.worker.NotificationUtils.createNotificationChannel
import com.thomas.apps.dailywallpaper.worker.NotificationUtils.showNotificationDelay
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()

        setUpCoil()

        applicationContext.createNotificationChannel()

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.VERBOSE) // Or yourdesired level
            .build()
        WorkManager.initialize(this, config)

        startAutoWallpaperWorker()
    }

    private fun startAutoWallpaperWorker() {


        val calendar15h = Calendar.getInstance(Locale.getDefault())
        calendar15h.set(Calendar.HOUR_OF_DAY, 15)
        calendar15h.set(Calendar.MINUTE, 0)
        calendar15h.set(Calendar.SECOND, 0)
        calendar15h.set(Calendar.MILLISECOND, 0)

        val fifteenHoursInMillis: Long = 15 * 60 * 60 * 1000L

        val now = Calendar.getInstance(Locale.getDefault())

        val todayRemain: Long = (
                (23 - now.get(Calendar.HOUR_OF_DAY)) * 60 * 60 * 1000 +
                        (59 - now.get(Calendar.MINUTE)) * 60 * 1000 +
                        (59 - now.get(Calendar.SECOND)) * 1000 +
                        (999 - now.get(Calendar.MILLISECOND))
                ).toLong()

        val diff = calendar15h.timeInMillis - now.timeInMillis
        val delayMillis: Long = if (diff < 0) {
            setWallpaperNow()
            todayRemain + fifteenHoursInMillis
        } else {
            diff
        }

        val date = Date(delayMillis)
        val spf = SimpleDateFormat("HH:mm:ss.SSS dd-MM-yyyy", Locale.getDefault())

        val timeSet = Date(delayMillis + Date().time)

        val delayInHours = delayMillis.toDouble() / 1000.0 / 60 / 60
        val string = String.format("%.2f", delayInHours)
        applicationContext.showNotificationDelay("Delay $string hours - ${spf.format(timeSet)}")
//        applicationContext.showNotificationDelay("delay ${spf.format(date)}")
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
                ExistingPeriodicWorkPolicy.REPLACE,
                setWallpaperWorkRequest
            )
    }

    private fun setWallpaperNow() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            //.setRequiresCharging(true)
            .build()

        val setWallpaperWorkRequest =
            OneTimeWorkRequestBuilder<AutoWallpaperWorker>()
                .addTag("set wallpaper now")
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(this)
            .enqueueUniqueWork(
                "set wallpaper now",
                ExistingWorkPolicy.REPLACE,
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
                    add(ImageDecoderDecoder(applicationContext))
                } else {
                    add(GifDecoder())
                }
                add(SvgDecoder(applicationContext))

            }
            .build()
        Coil.setImageLoader(imageLoader)
    }
}