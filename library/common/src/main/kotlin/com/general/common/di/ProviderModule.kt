package com.general.common.di

import android.content.Context
import com.general.common.provider.LocationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.components.ViewWithFragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class, ViewModelComponent::class, ViewWithFragmentComponent::class)
object ProviderModule {
    @Provides
    fun provideLocationProvider(@ApplicationContext context: Context) = LocationProvider(context)
}