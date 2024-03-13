plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}


android {
    namespace = "com.general.library.unittest"
    compileSdk = Release.MAX_SDK

    defaultConfig {
        minSdk = Release.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            enableUnitTestCoverage =  true
            enableAndroidTestCoverage = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFile("consumer-rules.pro")
        }
        debug {
            isMinifyEnabled = false
            enableUnitTestCoverage =  true
            enableAndroidTestCoverage = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFile("consumer-rules.pro")
        }
        create("preprod") {
            isMinifyEnabled = false
            enableUnitTestCoverage =  true
            enableAndroidTestCoverage = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFile("consumer-rules.pro")
        }
        create("qa") {
            isMinifyEnabled = false
            enableUnitTestCoverage =  true
            enableAndroidTestCoverage = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFile("consumer-rules.pro")
        }
        create("localhost") {
            isMinifyEnabled = false
            enableUnitTestCoverage =  true
            enableAndroidTestCoverage = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFile("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(Modules.Library.COMMON))

    implementation(libs.mockitoCore)
    implementation(libs.junit)
    implementation(libs.junit.test)
    implementation(libs.junitjupiter.test)
    implementation(libs.coroutinesTest)

    implementation(libs.mockk)
    implementation(libs.roboElectric)

    implementation(libs.espresso)
    implementation(libs.testRunner)
    implementation(libs.testRules)
    implementation(libs.junit)
    implementation(libs.mockitoCore)
    implementation(libs.archCore)
}

allprojects {
    configurations.all {
        resolutionStrategy.force("org.objenesis:objenesis:2.6")
    }
}