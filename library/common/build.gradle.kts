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
    implementation(project(Modules.Model.COMMON))
    implementation(project(Modules.NAVIGATION))

    implementation(libs.retrofit)

    implementation(libs.cameraView)
    implementation(libs.cameraCore)
    implementation(libs.camera2)
    implementation(libs.cameraLifeCycle)

    implementation(libs.gcacaceSignaturePad)
    implementation(libs.dhavaImagePicker)
    implementation(libs.zeloryCompressor)
    implementation(libs.photoView)

//    implementation(platform(libs.firebaseBOM))
//    implementation(libs.firebaseCrashlytics)
//    implementation(libs.firebaseAnalytics)
//    implementation(libs.firebaseConfig)
}
