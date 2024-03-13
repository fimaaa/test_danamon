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
    implementation(project(Modules.Library.COMMON))
    implementation(project(Modules.Model.COMMON))
    implementation(project(Modules.NAVIGATION))
    implementation(project(Modules.REPOSITORY))

    implementation(project(Modules.Feature.JSON_PLACEHOLDER))
    implementation(project(Modules.Feature.ADMIN))

//    implementation(platform(LibraryAndroid.firebaseBOM))
//    implementation(LibraryAndroid.firebaseDatabase)
//    implementation(LibraryAndroid.firebaseConfig)
}