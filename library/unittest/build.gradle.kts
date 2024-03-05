plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}


android {
    namespace = "com.general.library.unittest"
    compileSdk = Release.maxSDK

    defaultConfig {
        minSdk = Release.minSDK

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
    implementation(project(Modules.common))

    implementation(LibraryAndroidTesting.mockitoCore)
    implementation(LibraryAndroidTesting.jUnit)
    implementation(LibraryAndroidTesting.jUnitTest)
    implementation(LibraryAndroidTesting.jUnitJupiterTest)
    implementation(LibraryAndroidTesting.coroutinesTest)

    implementation(LibraryAndroidTesting.mockk)
    implementation(LibraryAndroidTesting.roboElectric)

    implementation(LibraryAndroidTesting.espresso)
    implementation(LibraryAndroidTesting.testRunner)
    implementation(LibraryAndroidTesting.testRules)
    implementation(LibraryAndroidTesting.jUnit)
    implementation(LibraryAndroidTesting.mockitoCore)
    implementation(LibraryAndroidTesting.archCore)
}

allprojects {
    configurations.all {
        resolutionStrategy.force("org.objenesis:objenesis:2.6")
    }
}