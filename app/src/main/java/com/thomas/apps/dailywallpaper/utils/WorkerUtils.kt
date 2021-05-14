package com.thomas.apps.dailywallpaper.utils

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import com.thomas.apps.dailywallpaper.R

object WorkerUtils {
    fun Context.isBatteryOptimized(): Boolean {
        val powerManager = this.getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = packageName
        return !powerManager.isIgnoringBatteryOptimizations(name)
    }

    fun Context.checkBattery() {
        val name = resources.getString(R.string.app_name)
        Toast.makeText(this, "Battery optimization -> All apps -> $name -> Don't optimize", Toast.LENGTH_LONG).show()

        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        startActivity(intent)
    }
}