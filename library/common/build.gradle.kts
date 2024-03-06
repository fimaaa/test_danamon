plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    id("com.google.firebase.crashlytics")
}

apply(from = "${project.rootDir}/library/common/android_common.gradle")
apply(from = "${project.rootDir}/library/common/android_core_dependencies.gradle")

android {
    packagingOptions {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module"
            )
        }
    }

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    namespace = "com.general.common"
}

dependencies {
    implementation(project(Modules.modelCommon))
    implementation(project(Modules.navigation))

    implementation(LibraryAndroid.retrofit)

    implementation(LibraryAndroid.cameraView)
    implementation(LibraryAndroid.cameraCore)
    implementation(LibraryAndroid.camera2)
    implementation(LibraryAndroid.cameraLifeCycle)

    implementation(LibraryAndroid.gcacaceSignaturePad)
    implementation(LibraryAndroid.dhavaImagePicker)
    implementation(LibraryAndroid.zeloryCompressor)
    implementation(LibraryAndroid.photoView)

//    implementation(platform(LibraryAndroid.firebaseBOM))
//    implementation(LibraryAndroid.firebaseCrashlytics)
//    implementation(LibraryAndroid.firebaseAnalytics)
//    implementation(LibraryAndroid.firebaseConfig)
}
