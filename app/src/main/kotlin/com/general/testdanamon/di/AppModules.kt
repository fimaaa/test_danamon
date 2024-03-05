package com.general.testdanamon.di

import cat.ereza.customactivityoncrash.config.CaocConfig
import com.general.testdanamon.BuildConfig
import com.general.testdanamon.CrashActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.general.common.R as commonR

@InstallIn(SingletonComponent::class)
@Module
object AppModules {
    @Singleton
    @Provides
    fun provideDefaultCaocConfigBuilder(): CaocConfig.Builder = CaocConfig.Builder.create()
        .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM) // default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
        .enabled(true) // default: true
        .showErrorDetails(BuildConfig.VARIANT != "release") // default: true
        .showRestartButton(false) // default: true
        .logErrorOnRestart(false) // default: true
        .trackActivities(true) // default: false
        .minTimeBetweenCrashesMs(2000) // default: 3000
        .errorDrawable(commonR.drawable.ic_error_button) // default: bug image
        .restartActivity(CrashActivity::class.java) // default: null (your app's launch activity)
        .errorActivity(CrashActivity::class.java) // default: null (default error activity)
//            .eventListener(YourCustomEventListener()) // default: null
//            .customCrashDataCollector(YourCustomCrashDataCollector()) // default: null

//    @Provides
//    fun provideFirebaseDependency(): AppPreference {
//        return SomeDependency()
//    }
}