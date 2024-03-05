package com.general.network.di

external fun getBaseUrlAll(): String
object ExternalData {
    init {
        System.loadLibrary("native-lib")
    }
}

fun getBaseUrl(): String = getBaseUrlAll()
