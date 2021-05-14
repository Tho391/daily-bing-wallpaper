package com.thomas.apps.dailywallpaper.utils.network

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = "No Network Connection"
}