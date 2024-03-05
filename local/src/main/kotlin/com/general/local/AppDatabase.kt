package com.general.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.general.local.converter.Converters
import com.general.local.dao.MemberDao
import com.general.model.common.user.MemberLocal

@Database(
    entities = [
        MemberLocal::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // DAO
    abstract fun getMemberDao(): MemberDao
}