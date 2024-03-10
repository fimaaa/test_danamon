plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

apply("${project.rootDir}/library/common/android_common.gradle")

android {
    namespace = "com.general.network"

    externalNativeBuild {
        cmake {
            path =  file("CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    implementation(project(Modules.common))
    implementation(project(Modules.modelCommon))
    implementation(project(Modules.local))

    implementation(project(Modules.modelJsonPlaceHolder))

    implementation(LibraryAndroid.androidXDataStore)

    implementation(LibraryAndroid.retrofit)
    implementation(LibraryAndroid.retrofitRX)
    implementation(LibraryAndroid.retrofitCoroutines)
    implementation(LibraryAndroid.retrofitConverterGson)
    implementation(LibraryAndroid.rxAndroid)
    implementation(LibraryAndroid.rxJava)

    implementation(LibraryAndroid.moshi)
    implementation(LibraryAndroid.moshiKotlin)
    implementation(LibraryAndroid.retrofitConverterMoshi)

    implementation(platform(LibraryAndroid.okhttpPlatform))
    implementation(LibraryAndroid.okHttp)
    implementation(LibraryAndroid.okhttpLogging)

    // Dagger Hilt
    kaptAndroidTest(LibraryAndroidTesting.dagger)
    // Hilt dependencies
    implementation(LibraryAndroid.daggerHilt)
    kapt(LibraryAndroid.daggerHiltCompiler)

    //HTTP Inspector
    "localhostImplementation"(LibraryAndroid.chuckerActive)
    debugImplementation(LibraryAndroid.chuckerActive)
    "qaImplementation"(LibraryAndroid.chuckerActive)
    "preprodImplementation"(LibraryAndroid.chuckerActive)
    releaseImplementation(LibraryAndroid.chuckerDisable)

    implementation(LibraryAndroid.gson)

    implementation(platform(LibraryAndroid.firebaseBOM))
    implementation(LibraryAndroid.firebaseDatabase)
}