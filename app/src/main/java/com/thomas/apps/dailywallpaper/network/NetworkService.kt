package com.thomas.apps.dailywallpaper.network

import android.app.Application
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.thomas.apps.dailywallpaper.network.param.UserParam
import com.thomas.apps.dailywallpaper.network.response.UserResponse
import com.thomas.apps.dailywallpaper.network.response.bing.BingResponse
import com.thomas.apps.dailywallpaper.utils.network.ApiResponse
import com.thomas.apps.dailywallpaper.utils.network.LiveDataCallAdapterFactory
import com.thomas.apps.dailywallpaper.utils.network.NetworkConnectionInterceptor
import com.thomas.apps.dailywallpaper.utils.network.NullOnEmptyConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface NetworkService {

    @GET(EndPoints.USERS)
    fun requestUsers(): LiveData<ApiResponse<List<UserResponse>>>

    @GET(EndPoints.USERS)
    fun requestUserById(@Path("userId") userId: String): LiveData<ApiResponse<UserResponse>>

    @POST(EndPoints.USERS)
    fun requestCreateUser(@Body userParam: UserParam): LiveData<ApiResponse<UserResponse>>

    @PUT(EndPoints.USERS)
    fun requestUpdateUser(@Body userParam: UserParam): LiveData<ApiResponse<UserResponse>>

    @DELETE(EndPoints.USERS)
    fun requestDeleteUser(@Body userParam: UserParam): LiveData<ApiResponse<UserResponse>>

    @Headers(
        "Access-Control-Allow-Origin: *"
    )
    @GET(EndPoints.WALL_PAPER)
    suspend fun requestWallpaper(
        @Query("idx") index: Int = 0,
        @Query("n") numberOfPicture: Int = 8,
        @Query("format") format: String = "js"
    ): Response<BingResponse>

    companion object {
        private const val API_URL = "https://www.bing.com/"

        fun create(application: Application): NetworkService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val networkConnectionInterceptor = NetworkConnectionInterceptor(application)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(networkConnectionInterceptor)

            val useMockData = true
            val client = if (useMockData) {
                val mockInterceptor = MockDataInterceptor()
                okHttpClient.addInterceptor(mockInterceptor)
            } else {
                //add token auth here
                okHttpClient
            }


            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client.build())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(NetworkService::class.java)
        }
    }
}