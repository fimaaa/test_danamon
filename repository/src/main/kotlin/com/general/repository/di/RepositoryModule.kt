package com.general.repository.di

import com.general.repository.repository.JsonPlaceHolderRepository
import com.general.repository.repository.JsonPlaceHolderRepositoryImpl
import com.general.repository.repository.SessionRepository
import com.general.repository.repository.SessionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class, ViewModelComponent::class)
@Module
interface RepositoryModule {
//    @Singleton
//    @Provides
//    fun provideSettingRepository(
//        api: SettingApi,
//        local: SettingDao
//    ) = SettingRepositoryImpl(api, local) as SettingRepository

    @Binds
    fun bindsSessionsRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository

    @Binds
    fun bindOrderJsonPlaceHolderRepository(
        settingRepositoryImpl: JsonPlaceHolderRepositoryImpl
    ): JsonPlaceHolderRepository

//    @Singleton
//    @Provides
//    fun provideDefaultEmployeeRepository(
//        exampleDao: EmployeeDao,
//        movieApi: EmployeeApi
//    ) = EmployeeRepositoryImpl(exampleDao, movieApi) as EmployeeRepository
//
//    @Singleton
//    @Provides
//    fun provideDefaultTopListRepository(
//        datasource: TopListDataSource,
//        dao: CryptoDao,
//        remoteKeyDao: RemoteKeysDao
//    ) = TopListRepositoryImpl(datasource, dao, remoteKeyDao) as TopListRepository
}