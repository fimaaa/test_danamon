package com.general.common.base

import android.app.Application
import android.content.Context

abstract class BaseApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: BaseApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}