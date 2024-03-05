package com.general.common.di

import com.general.common.GeneralDefaultDispatcher
import com.general.common.GeneralIODispatcher
import com.general.common.GeneralMainDispatcher
import com.general.common.GeneralMainImmediateDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.components.ViewWithFragmentComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class, ActivityComponent::class, FragmentComponent::class, ViewModelComponent::class, ViewWithFragmentComponent::class)
object DispatchersModule {

    @Provides
    @GeneralDefaultDispatcher
    internal fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @GeneralIODispatcher
    internal fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @GeneralMainDispatcher
    internal fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @GeneralMainImmediateDispatcher
    internal fun provideMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main
}