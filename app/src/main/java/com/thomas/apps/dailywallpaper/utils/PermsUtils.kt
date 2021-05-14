package com.thomas.apps.dailywallpaper.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


object PermsUtils {
    private val bluetoothPerms =
        listOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)

    @RequiresApi(Build.VERSION_CODES.Q)
    const val backgroundLocationPerm = Manifest.permission.ACCESS_BACKGROUND_LOCATION

    const val cameraPerm = Manifest.permission.CAMERA

    @RequiresApi(Build.VERSION_CODES.Q)
    fun Activity.isBackgroundLocationPermissionGrant(): Boolean {
        return when (ContextCompat.checkSelfPermission(this, backgroundLocationPerm)) {
            PERMISSION_GRANTED -> {
                true
            }
            else -> {
                false
            }
        }
    }
}