package com.thomas.apps.dailywallpaper.network

import com.thomas.apps.dailywallpaper.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber

class MockDataInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return when {
            BuildConfig.DEBUG -> {
                val uri = chain.request().url.toUri().toString()
                val requestMethod = chain.request().method
                Timber.i("request method: $requestMethod")

                val responseString =
                    when (requestMethod) {
                        "GET" -> {
                            when {
                                uri.endsWith("user") -> {
                                    ""
                                }
                                else -> {
                                    ""
                                }
                            }
                        }
                        "POST" -> {
                            when {
                                //create user
                                uri.endsWith("user") -> {
                                    ""
                                }
                                else -> {
                                    ""
                                }
                            }
                        }
                        "PUT" -> {
                            when {
                                //edit user
                                uri.endsWith("user") -> {
                                    ""
                                }
                                else -> {
                                    ""
                                }
                            }
                        }
                        "PATH" -> {
                            when {
                                uri.endsWith("user") -> {
                                    ""
                                }
                                else -> {
                                    ""
                                }
                            }
                        }
                        "DELETE" -> {
                            when {
                                //delete user
                                uri.endsWith("user") -> {
                                    ""
                                }
                                else -> {
                                    ""
                                }
                            }
                        }
                        else -> {
                            ""
                        }
                    }

                val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
                val responseBody = responseString.toResponseBody(mediaType)

                chain.proceed(chain.request())
                    .newBuilder()
                    .code(200)
                    .protocol(Protocol.HTTP_2)
                    .body(responseBody)
                    .addHeader("content-type", "application/json")
                    .build()
            }
            else -> {
                chain.proceed(chain.request())
            }
        }
    }
}