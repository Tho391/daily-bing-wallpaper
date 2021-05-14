package com.thomas.apps.dailywallpaper.repository

import androidx.lifecycle.LiveData
import com.thomas.apps.dailywallpaper.model.User
import com.thomas.apps.dailywallpaper.utils.network.RateLimiter
import com.thomas.apps.dailywallpaper.utils.network.Resource

interface UserRepository {
    val requestRateLimit: RateLimiter<String>

    //user
    fun getUser(): LiveData<Resource<User>>

}