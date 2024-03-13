plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply("${project.rootDir}/library/common/android_common.gradle")
apply("${project.rootDir}/library/common/android_core_dependencies.gradle")

android {
    namespace = "com.general.jsonplaceholder"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(Modules.Library.COMMON))
    implementation(project(Modules.Model.COMMON))
    implementation(project(Modules.NAVIGATION))
    implementation(project(Modules.REPOSITORY))

    implementation(project(Modules.Model.JSON_PLACEHOLDER))
}