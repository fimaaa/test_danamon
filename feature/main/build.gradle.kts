plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply("${project.rootDir}/library/common/android_common.gradle")
apply("${project.rootDir}/library/common/android_core_dependencies.gradle")

android {
    namespace = "com.general.testdanamon.main"
}

dependencies {
    implementation(project(Modules.common))
    implementation(project(Modules.modelCommon))
    implementation(project(Modules.navigation))
    implementation(project(Modules.repository))

    implementation(project(Modules.featureJsonPlaceHolder))
    implementation(project(Modules.featureAdmin))

//    implementation(platform(LibraryAndroid.firebaseBOM))
//    implementation(LibraryAndroid.firebaseDatabase)
//    implementation(LibraryAndroid.firebaseConfig)
}