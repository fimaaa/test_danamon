plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

apply("${project.rootDir}/library/common/android_common.gradle")

android {
    namespace = "com.general.model.common"
}

dependencies {

    implementation(platform(LibraryAndroid.composeBom))
    androidTestImplementation(platform(LibraryAndroid.composeBom))

    implementation(LibraryAndroid.composeMaterial3)

    implementation(LibraryAndroid.moshi)
    implementation(LibraryAndroid.moshiKotlin)
    implementation(LibraryAndroid.gson)

    implementation(LibraryAndroid.roomRuntime)
    annotationProcessor(LibraryAndroid.roomCompiler)
    kapt(LibraryAndroid.roomCompiler)
    implementation(LibraryAndroid.room)
    testImplementation(LibraryAndroid.roomTesting)

//    implementation(LibraryAndroid.roomCommon)
}