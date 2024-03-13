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
    implementation(project(Modules.Library.COMMON))
    implementation(project(Modules.Model.COMMON))
    implementation(project(Modules.LOCAL))

    implementation(project(Modules.Model.JSON_PLACEHOLDER))

    implementation(libs.androidXDataStore)

    implementation(libs.retrofit)
    implementation(libs.retrofitRX)
    implementation(libs.retrofitCoroutines)
    implementation(libs.retrofitConverterGson)
    implementation(libs.rxAndroid)
    implementation(libs.rxJava)

    implementation(libs.moshi)
    implementation(libs.moshiKotlin)
    implementation(libs.retrofitConverterMoshi)

    implementation(platform(libs.okhttpPlatform))
    implementation(libs.okHttp)
    implementation(libs.okhttpLogging)

    // Dagger Hilt
    kaptAndroidTest(libs.dagger)
    // Hilt dependencies
    implementation(libs.daggerHilt)
    kapt(libs.daggerHiltCompiler)

    //HTTP Inspector
    "localhostImplementation"(libs.chuckerActive)
    debugImplementation(libs.chuckerActive)
    "qaImplementation"(libs.chuckerActive)
    "preprodImplementation"(libs.chuckerActive)
    releaseImplementation(libs.chuckerDisable)

    implementation(libs.gson)

//    implementation(platform(libs.firebaseBOM))
//    implementation(libs.firebaseDatabase)
}