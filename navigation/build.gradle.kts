plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
}
apply("${project.rootDir}/library/common/android_common.gradle")

android {
    namespace = "com.general.navigation"
}

dependencies {
    implementation(LibraryAndroid.navigationFragment)
    implementation(LibraryAndroid.navigationUi)
}