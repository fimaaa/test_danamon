@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
    // id("com.google.firebase.crashlytics")
}

// Apply external build scripts
apply {
    from("${project.rootDir}/library/common/android_common.gradle")
    from("${project.rootDir}/library/common/android_core_dependencies.gradle")
}

android {
    namespace = "com.general.testdanamon"
    packagingOptions {
        resources {
            excludes += listOf(
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
        applicationId = "com.general.testdanamon"
        versionCode = Release.VERSION_CODE
        versionName = Release.VERSION_APP_NAME
        externalNativeBuild {
            cmake {
                cppFlags
            }
        }
        signingConfig = signingConfigs.getByName("debug")
        applicationVariants.all {
            outputs.forEach { output ->
                if (output is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                    var outputFileName = "test-danamon-${this.versionName}.${output.outputFile.extension}"
                    output.outputFileName = outputFileName
                }
            }
        }
    }

    buildTypes {
        getByName("release") {
            resValue("string", "version_name", Release.VERSION_APP_NAME)
            resValue("string", "app_name", Release.APP_NAME)
            buildConfigField("String", "VARIANT", "\"release\"")
            isMinifyEnabled = true
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        named("preprod") {
            resValue("string", "version_name", "${Release.VERSION_APP_NAME}-preprod")
            resValue("string", "app_name", "${Release.APP_NAME}-preprod")
            buildConfigField("String", "VARIANT", "\"preprod\"")
            versionNameSuffix = "-preprod"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        named("qa") {
            resValue("string", "version_name", "${Release.VERSION_APP_NAME}-qa")
            resValue("string", "app_name", "${Release.APP_NAME}-qa")
            buildConfigField("String", "VARIANT", "\"qa\"")
            isMinifyEnabled = true
            isShrinkResources = true
            versionNameSuffix = "-qa"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        named("debug") {
            resValue("string", "version_name", "${Release.VERSION_APP_NAME}-dev")
            resValue("string", "app_name", "${Release.APP_NAME}-dev")
            buildConfigField("String", "VARIANT", "\"development\"")
            versionNameSuffix = "-dev"
        }
        named("localhost") {
            resValue("string", "version_name", "${Release.VERSION_APP_NAME}-local")
            resValue("string", "app_name", "${Release.APP_NAME}-local")
            buildConfigField("String", "VARIANT", "\"development\"")
            versionNameSuffix = "-local"
        }
    }
}

dependencies {
    implementation(project(Modules.Library.COMMON))
    implementation(project(Modules.Model.COMMON))
    implementation(project(Modules.NAVIGATION))
    implementation(project(Modules.REPOSITORY))
    implementation(project(Modules.NETWORK))
    implementation(project(Modules.LOCAL))
    implementation(project(Modules.Feature.MAIN))
    implementation(project(Modules.Feature.SPLASHSCREEN))
    implementation(project(Modules.Feature.AUTH))
    implementation(project(Modules.Feature.JSON_PLACEHOLDER))
    implementation(project(Modules.Feature.ADMIN))

    testImplementation(project(Modules.Library.COMMON))
    androidTestImplementation(project(Modules.Library.COMMON))

    // Networking
    // Retrofit + GSON
    implementation(libs.retrofit)
    implementation(libs.retrofitConverterGson)
    implementation(libs.retrofitRX)
    implementation(libs.retrofitConverterMoshi)
    implementation(libs.retrofitCoroutines)

    implementation(platform(libs.okhttpPlatform))
    implementation(libs.okHttp)
    implementation(libs.okhttpLogging)

    // Room
    implementation(libs.roomRuntime)
    annotationProcessor(libs.roomCompiler)
    kapt(libs.roomCompiler)
    implementation(libs.room)
    testImplementation(libs.roomTesting)

    //HTTP Inspector
    implementation(libs.chuckerActive)

    // Google
    implementation(libs.gson)

    implementation(libs.customCrash)
}

kapt {
    correctErrorTypes = true
}