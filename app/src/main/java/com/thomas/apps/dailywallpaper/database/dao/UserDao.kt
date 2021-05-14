package com.thomas.apps.dailywallpaper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bosch.aquaeasyshrimpfarming.database.dao.BaseDao
import com.thomas.apps.dailywallpaper.database.entity.UserEntity

@Dao
interface UserDao: BaseDao<UserEntity> {

    @Query("select * from user")
    fun getUsers(): LiveData<List<UserEntity>>
}