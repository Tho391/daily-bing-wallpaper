package com.thomas.apps.dailywallpaper.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (context.isNetworkConnected().not()) {
            throw NoConnectivityException()
        }

        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private fun Context.isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork

        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)

        val isNetworkConnected = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        if (isNetworkConnected == true)
            return true
        return false
    }
}