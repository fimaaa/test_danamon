package com.general.network.di

import com.general.model.common.enum.EnumBuild
import com.general.network.BuildConfig

external fun getJsonPlaceHolderBaseUrl(): String
external fun getBaseAppBaseLocalUrl(): String
external fun getPathAuth(): String
external fun getPathMember(): String
object ExternalData {
    init {
        System.loadLibrary("native-lib")
    }
}

fun getBaseUrl(): String = when {
    BuildConfig.BUILD_TYPE.equals(EnumBuild.RELEASE.name, true) -> getBaseAppBaseLocalUrl()
    else -> getBaseAppBaseLocalUrl()
}