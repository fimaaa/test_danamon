package com.general.library.unittest.util

import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

@Implements(System::class)
class RoboShadow {
    companion object {
        @JvmStatic
        @Suppress("unused")
        @Implementation
        fun loadLibrary(libraryName: String): Boolean {
            println("TAG LOADLIBRARY $libraryName")
            // Provide a custom implementation to load the native library
            if (libraryName == "native-lib") {
                System.loadLibrary("native-lib")
                return true
            }
            return false
        }
    }
}