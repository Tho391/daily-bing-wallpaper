package com.thomas.apps.dailywallpaper.utils.network

import android.app.Application
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val application: Application,
) :
    Authenticator {

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
    }

    override fun authenticate(route: Route?, response: Response): Request? {

//        val future = CompletableFuture<Request?>()
//        val mainApp = application as MainApplication
//        val authState = mainApp.authStateManager.current
//        val authService = mainApp.authService
//
//        authState.performActionWithFreshTokens(authService) { accessToken, _, ex ->
//            if (ex != null) {
//                Timber.e(ex)
//            }
//
//            if (response.request.header(HEADER_AUTHORIZATION) != null) {
//                future.complete(null)
//            }
//
//            val authResponse = response.request.newBuilder()
//                .header(HEADER_AUTHORIZATION, "Bearer $accessToken")
//                .build()
//
//            future.complete(authResponse)
//        }
//        return future.get()

        return null
    }
}