package com.general.local.di

import android.content.Context
import androidx.room.Room
import com.general.local.AppDatabase
import com.general.local.AppPreference
import com.general.local.dao.MemberDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {

    @Singleton
    @Provides
    fun provideTestDanamonDatabase(
        @ApplicationContext app: Context
    ): AppDatabase = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "general_test_danamon"
    ).build()

    @Singleton
    @Provides
    fun provideMemberDao(db: AppDatabase): MemberDao = db.getMemberDao()

    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context): AppPreference {
        return AppPreference(context)
    }
}