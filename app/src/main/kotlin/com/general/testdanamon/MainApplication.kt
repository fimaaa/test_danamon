package com.general.testdanamon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.general.common.base.BaseApplication
import com.general.model.common.enum.EnumBuild
import com.general.model.common.enum.Language
import com.general.network.di.ExternalData
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : BaseApplication(), LifecycleEventObserver {

    var lifeCycleApplication: Lifecycle.State = Lifecycle.State.DESTROYED

    @Inject
    lateinit var caocConfig: CaocConfig.Builder

    override fun onCreate() {
        super.onCreate()
        ExternalData
        caocConfig.apply()
        configNotificationChannel()
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(
            when (BuildConfig.BUILD_TYPE) {
                EnumBuild.DEBUG.id, EnumBuild.LOCAL.id -> false
                else -> true
            }
        )
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun attachBaseContext(base: Context) {
        val context = updateBaseContextLocale(base)
        super.attachBaseContext(context)
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val locale = Language.INDONESIA.toLocale()
        Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun configNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            // Channel Chucker
            val channelChuckerId = "chucker_notification_channel"
            val channelChuckerlName = "Chucker Notifications"
            val channelChuckerlDescription = "Notification channel for Chucker"
            val importanceChuckerl = NotificationManager.IMPORTANCE_LOW
            val channelChuckerl = NotificationChannel(
                channelChuckerId,
                channelChuckerlName,
                importanceChuckerl
            ).apply {
                description = channelChuckerlDescription
                // Additional channel configuration if needed
            }

            notificationManager.createNotificationChannel(channelChuckerl)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        lifeCycleApplication = source.lifecycle.currentState
    }
}