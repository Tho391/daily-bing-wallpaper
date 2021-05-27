package com.thomas.apps.dailywallpaper.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.thomas.apps.dailywallpaper.R

object NotificationUtils {
    const val CHANNEL_ID = "Set wallpaper"
    const val CHANNEL_NAME = "Wallpaper channel"
    const val CHANNEL_DESRIPTION = "This is wallpaper channel description"

    const val NOTIFICATION_ID = 100

    const val CHANNEL_DELAY_ID = "Set wallpaper delay"
    const val CHANNEL_DELAY_NAME = "Wallpaper channel delay"
    const val NOTIFICATION_DELAY_ID = 101

    fun Context.createNotification(content: String): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_wallpaper)
            .setContentTitle("Set wallpaper")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder.build()
    }

    fun Context.createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val channel2 = NotificationChannel(CHANNEL_DELAY_ID, CHANNEL_DELAY_NAME, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.createNotificationChannel(channel2)
        }
    }

    fun Context.showNotification(content: String) {
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, createNotification(content))
        }
    }

    fun Context.showNotificationDelay(content: String) {
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_DELAY_ID, createNotification(content))
        }
    }
}