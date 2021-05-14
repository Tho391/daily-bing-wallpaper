package com.bosch.aquaeasyshrimpfarming.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(vararg obj: T): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(obj: List<T>): List<Long>

    @Delete
    fun delete(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSuspend(vararg obj: T) : List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSuspend(obj: List<T>): List<Long>

    @Delete
    suspend fun deleteSuspend(vararg obj: T): Int

}