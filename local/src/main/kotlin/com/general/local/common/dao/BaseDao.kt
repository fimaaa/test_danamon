package com.general.local.common.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: T)
}