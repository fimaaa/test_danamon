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
                "META-INF/*.kotlin_module",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module",
                "AndroidManifest.xml"
            )
        }
        jniLibs {
            excludes += setOf(
                "lib/arm64-v8a/libcardioDecider.so",
                "lib/arm64-v8a/libcardioRecognizer.so",
                "lib/arm64-v8a/libcardioRecognizer_tegra2.so",
                "lib/arm64-v8a/libopencv_core.so",
                "lib/arm64-v8a/libopencv_imgproc.so",
                "lib/armeabi/libcardioDecider.so",
                "lib/armeabi-v7a/libcardioDecider.so",
                "lib/armeabi-v7a/libcardioRecognizer.so",
                "lib/armeabi-v7a/libcardioRecognizer_tegra2.so",
                "lib/armeabi-v7a/libopencv_core.so",
                "lib/armeabi-v7a/libopencv_imgproc.so",
                "lib/mips/libcardioDecider.so",
                "lib/x86/libcardioDecider.so",
                "lib/x86/libcardioRecognizer.so",
                "lib/x86/libcardioRecognizer_tegra2.so",
                "lib/x86/libopencv_core.so",
                "lib/x86/libopencv_imgproc.so",
                "lib/x86_64/libcardioDecider.so",
                "lib/x86_64/libcardioRecognizer.so",
                "lib/x86_64/libcardioRecognizer_tegra2.so",
                "lib/x86_64/libopencv_core.so",
                "lib/x86_64/libopencv_imgproc.so"
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
