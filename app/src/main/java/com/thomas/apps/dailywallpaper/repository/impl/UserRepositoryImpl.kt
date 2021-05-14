package com.thomas.apps.dailywallpaper.repository.impl

import androidx.lifecycle.LiveData
import com.thomas.apps.dailywallpaper.model.User
import com.thomas.apps.dailywallpaper.repository.UserRepository
import com.thomas.apps.dailywallpaper.utils.network.RateLimiter
import com.thomas.apps.dailywallpaper.utils.network.Resource
import java.util.concurrent.TimeUnit

class UserRepositoryImpl : UserRepository {
    override val requestRateLimit: RateLimiter<String>
        get() = RateLimiter(10, TimeUnit.MINUTES)

    override fun getUser(): LiveData<Resource<User>> {
        TODO("Not yet implemented")

    }
}