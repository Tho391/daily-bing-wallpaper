package com.thomas.apps.dailywallpaper.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.thomas.apps.dailywallpaper.R

object NotificationUtils {
    const val DOWNLOAD_GROUP = "DOWNLOAD_GROUP"

    const val CHANNEL_ID = "Set wallpaper"
    const val CHANNEL_NAME = "Wallpaper channel"
    const val CHANNEL_DESRIPTION = "This is wallpaper channel description"

    const val NOTIFICATION_ID = 100

    const val CHANNEL_DELAY_ID = "Set wallpaper delay"
    const val CHANNEL_DELAY_NAME = "Wallpaper channel delay"
    const val NOTIFICATION_DELAY_ID = 101

    const val CHANNEL_DOWNLOAD_ID = "CHANNEL_DOWNLOAD_ID"
    const val CHANNEL_DOWNLOAD_NAME = "Download channel"
    const val NOTIFICATION_DOWNLOAD_ID = 102

    private fun Context.createNotification(content: String): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download_done)
            .setContentTitle("Set Wallpaper")
            .setContentText(content)
            .setAutoCancel(true)
        return builder.build()
    }

    private fun Context.createNotificationDelay(content: String): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_DELAY_ID)
            .setSmallIcon(R.drawable.ic_downloading)
            .setContentTitle("Delay Set Wallpaper")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setNotificationSilent()
            .setAutoCancel(true)
        return builder.build()
    }

    private fun Context.createNotificationDownload(content: Uri?): Notification {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(content, "image/*")
        }

        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntent(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        val builder = NotificationCompat.Builder(this, CHANNEL_DOWNLOAD_ID)
            .setSmallIcon(R.drawable.ic_download_done)
            .setContentTitle("Download")
            .setContentText("Downloaded: $content")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup(DOWNLOAD_GROUP)
        return builder.build()
    }

    fun Context.createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = CHANNEL_NAME
        val descriptionText = CHANNEL_DESRIPTION
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val channel2 = NotificationChannel(CHANNEL_DELAY_ID, CHANNEL_DELAY_NAME, importance).apply {
            description = descriptionText
        }

        val channelDownload =
            NotificationChannel(CHANNEL_DOWNLOAD_ID, CHANNEL_DOWNLOAD_NAME, importance).apply {
                description = descriptionText
            }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.createNotificationChannel(channel2)
        notificationManager.createNotificationChannel(channelDownload)
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
            notify(NOTIFICATION_DELAY_ID, createNotificationDelay(content))
        }
    }

    fun Context.showNotificationDownload(content: Uri?) {
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_DELAY_ID, createNotificationDownload(content))
        }
    }
}