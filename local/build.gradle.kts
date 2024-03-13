plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

apply("${project.rootDir}/library/common/android_common.gradle")

android {
    namespace = "com.general.local"
}

dependencies {
    implementation(project(Modules.Model.COMMON))

    implementation(project(Modules.Model.JSON_PLACEHOLDER))

    implementation(libs.gson)

    implementation(libs.paging3)

    implementation(libs.roomRuntime)
    annotationProcessor(libs.roomCompiler)
    kapt(libs.roomCompiler)
    implementation(libs.room)
    implementation(libs.roomPaging)
    testImplementation(libs.roomTesting)

    implementation(libs.androidXDataStore)

    // Dagger Hilt
    kaptAndroidTest(libs.dagger)
    // Hilt dependencies
    implementation(libs.daggerHilt)
    kapt(libs.daggerHiltCompiler)
}