object Modules {
    const val common = ":library:common"
    const val unitTest = ":library:unittest"
    const val navigation = ":navigation"
    const val modelCommon = ":model:common"
    const val network = ":network"
    const val local = ":local"
    const val repository = ":repository"
    const val featureMain = ":feature:main"
    const val featureSplashscreen = ":feature:splashscreen"
    const val featureAuth = ":feature:auth"

    const val modelJsonPlaceHolder = ":model:jsonplaceholder"
    const val featureJsonPlaceHolder = ":feature:jsonplaceholder"

    const val featureAdmin = ":feature:admin"
}

object Release {
    private const val versionMajor = 0
    private const val versionMinor = 0
    private const val versionPatch = 1
    const val versionCode: Int = versionMajor*10000 + versionMinor*100 + versionPatch
    const val versionAppName: String = "$versionMajor.$versionMinor.$versionPatch"
    const val minSDK = 21
    const val maxSDK = 33

    const val appName = "Test_Danamon"
}

object Versions {
    const val gradle = "7.4.0"
    const val gradlePlugin = "1.7.20"
    const val googleService = "4.3.15"
    const val firebaseCrashlytics = "2.9.1"
    const val firebaseBOM = "31.4.0"
    const val kotlinVersion = "1.6.21"
    const val navigation = "2.5.3"

    const val hiltGradle = "2.28.3-alpha"
    const val daggerVersion = "2.43" // 2.43
    const val appCompatVersion = "1.6.0"
    const val constraintLayoutVersion = "2.1.4"
    const val materialVersion = "1.6.0"
    const val espressoVersion = "3.4.0"
    const val glideVersion = "4.12.0"
    const val glideVector = "v2.0.0"
    const val junitVersion = "4.13.2"
    const val jUnitJupiterVersion =  "5.8.1"
    const val mockk = "1.10.6"
    const val roboElectric = "4.11.1"
    const val ktxVersion = "1.9.0"
    const val pagingVersion = "3.1.1"
    const val retrofitVersion = "2.9.0"
    const val retrofitCoroutines = "0.9.2"
    const val moshi = "1.14.0"
    const val testExtJunitVersion = "1.1.3"
    const val leakCanaryVersion = "2.7"
    const val chuckerVersion = "3.5.2"
    const val gooleAuth = "20.3.0"
    const val googleAds = "21.2.0"
    const val gooleMaps = "18.1.0"
    const val googleLocation = "21.0.0"
    const val googleFlexbox = "2.0.1"
    const val gson = "2.8.9"
    const val googleAR = "1.31.0"
    const val roomVersion = "2.5.0"
    const val androidXDataStore = "1.0.0"

    const val cameraXVersion = "1.1.0"

    const val test = "1.4.0"
    const val truth = "1.0.1"
    const val mockito = "2.23.4"
    const val coroutinesTest = "1.5.2"
    const val archCore = "2.1.0"

    const val recyclerView = "1.2.1"
    const val anko = "0.10.8"

    const val rxAndroid = "2.1.1"
    const val rxJava = "2.2.10"
    const val rxBinding = "2.2.0"

    const val okhttpLogging = "4.9.3"
    const val lifeCycle = "2.5.1"
    const val lifeCycleExt = "2.2.0"

    const val localeHelper = "1.5.1"

    const val skeletonLayout = "4.0.0"
    const val progressButton = "2.1.0"

    const val skyDovesSandwich = "1.3.5"
    const val timber = "5.0.1"

    const val gcacaceSignaturePad = "1.3.1"
    const val dhavaImagePicker = "2.1"
    const val zeloryCompressor = "3.0.1"

    const val multiDex = "2.0.1"

    const val composeLifeCycle = "2.6.0-alpha01"
    const val composeActivity = "1.5.1"
    const val composeViewModel = "2.5.1"
    const val webSocket = "1.5.1"
    const val swipeRefresh = "1.1.0"
    const val customCrash = "2.4.0"
}

object LibraryProject {
    const val gradle =  "com.android.tools.build:gradle:${Versions.gradle}"
    const val gradlePlugin =  "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.gradlePlugin}"
    const val navigation = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val daggerHilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltGradle}"
    const val googleService = "com.google.gms:google-services:${Versions.googleService}"

    // Add the Crashlytics Gradle plugin
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlytics}"
}

object LibraryAndroid {
    const val kotlinX = "androidx.core:core-ktx:$${Versions.ktxVersion}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompatVersion}"
    const val constrainLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
    const val googleMaterial = "com.google.android.material:material:${Versions.materialVersion}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val googleAR = "com.google.ar:core:${Versions.googleAR}"

    // Camera
    const val cameraView = "androidx.camera:camera-view:${Versions.cameraXVersion}"
    const val cameraCore = "androidx.camera:camera-core:${Versions.cameraXVersion}"
    const val camera2 = "androidx.camera:camera-camera2:${Versions.cameraXVersion}"
    const val cameraLifeCycle = "androidx.camera:camera-lifecycle:${Versions.cameraXVersion}"

    // Check Leak Memory
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanaryVersion}"

    // Networking
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val retrofitRX = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofitVersion}"
    const val retrofitCoroutines = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofitCoroutines}"
    const val retrofitConverterGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val retrofitConverterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofitVersion}"


    const val okhttpPlatform = "com.squareup.okhttp3:okhttp-bom:${Versions.okhttpLogging}"
    const val okHttp = "com.squareup.okhttp3:okhttp"
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor"

    // Room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val room = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val roomCommon = "androidx.room:room-common-ktx:${Versions.roomVersion}"
    const val roomTesting = "androidx.room:room-testing:${Versions.roomVersion}"
    const val roomPaging = "androidx.room:room-paging:${Versions.roomVersion}"

    // DataStore
    const val androidXDataStore = "androidx.datastore:datastore-preferences:${Versions.androidXDataStore}"


    // Lifecycle
    const val lifeCycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifeCycle}"
    const val lifeCycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifeCycle}"
    const val lifeCycleExt = "androidx.lifecycle:lifecycle-extensions:${Versions.lifeCycleExt}"

    // DataBinding
    const val dataBindingCompiler = "com.android.databinding:compiler:4.0.1" // 4.2.1
    const val rxBinding = "com.jakewharton.rxbinding2:rxbinding:${Versions.rxBinding}"

    // Navigation Component
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}" // 2.5.1
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}" // 2.5.1

    // ...with Kotlin.
    const val daggerHilt = "com.google.dagger:hilt-android:${Versions.daggerVersion}"
    const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.daggerVersion}"

    const val daggerHiltViewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha02"
    const val daggerHiltViewModelCompiler = "androidx.hilt:hilt-compiler:1.0.0-alpha02"

    // Glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"
    const val glideToVector = "com.github.corouteam:GlideToVectorYou:${Versions.glideVector}"

    // Change Language
    const val localeHelper = "com.zeugmasolutions.localehelper:locale-helper-android:${Versions.localeHelper}"

    // Paging 3
    const val paging3 = "androidx.paging:paging-runtime-ktx:${Versions.pagingVersion}"

    //HTTP Inspector
    const val chuckerActive = "com.github.chuckerteam.chucker:library:${Versions.chuckerVersion}"
    const val chuckerDisable = "com.github.chuckerteam.chucker:library-no-op:${Versions.chuckerVersion}"

    // Google
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val googleAuth = "com.google.android.gms:play-services-auth:${Versions.gooleAuth}"
    // Google Ads
    const val googleAds = "com.google.android.gms:play-services-ads:${Versions.googleAds}"
    // Google maps
    const val googleMaps = "com.google.android.gms:play-services-maps:${Versions.gooleMaps}"
    const val googleLocation =  "com.google.android.gms:play-services-location:${Versions.googleLocation}"
    const val googleFlexbox = "com.google.android:flexbox:${Versions.googleFlexbox}"

    // Declare the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    const val firebaseBOM = "com.google.firebase:firebase-bom:${Versions.firebaseBOM}"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
    const val firebaseNotification = "com.google.firebase:firebase-messaging-ktx"
    const val firebaseInAppMessage = "com.google.firebase:firebase-inappmessaging-display-ktx"
    const val firebaseDatabase = "com.google.firebase:firebase-database-ktx"
    const val firebaseConfig = "com.google.firebase:firebase-config-ktx"


    // Location Hash
    const val geoHash = "com.github.drfonfon:android-kotlin-geohash:1.0"

    // Style
    const val skeletonLayout = "com.faltenreich:skeletonlayout:${Versions.skeletonLayout}"
    const val progressButton = "com.github.razir.progressbutton:progressbutton:${Versions.progressButton}"


    // signature helper
    const val gcacaceSignaturePad = "com.github.gcacace:signature-pad:${Versions.gcacaceSignaturePad}"

    // imagepicker helper
    const val dhavaImagePicker = "com.github.dhaval2404:imagepicker:${Versions.dhavaImagePicker}"
    const val zeloryCompressor = "id.zelory:compressor:${Versions.zeloryCompressor}"

    /**
    Compose
     **/

    const val composeBom = "androidx.compose:compose-bom:2023.01.00"

    /* Choose only one Material */
    // Material Design 3
    const val composeMaterial3 = "androidx.compose.material3:material3"
    // Material Design 2
    const val composeMaterial2 = "androidx.compose.material:material"
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    const val composeFoundation = "androidx.compose.foundation:foundation"
    // or only import the main APIs for the underlying toolkit systems,
    //  such as input and measurement/layout
    const val composeUI = "androidx.compose.ui:ui"
    /* */

    // Tooling support (Previews, etc.)
    const val composeUIPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeUITool = "androidx.compose.ui:ui-tooling"

    // Material design icons add if not using material
    // but need icon
    const val composeMaterialIconCore = "androidx.compose.material:material-icons-core"
    // Optional - Add full set of material icons
    const val composeMaterialIconExt = "androidx.compose.material:material-icons-extended"
    // Optional - Add window size utils
    const val composeWindowsSize = "androidx.compose.material3:material3-window-size-class"


    // Integration with observables
    const val composeLiveData = "androidx.compose.runtime:runtime-livedata"
    const val composeRXJava = "androidx.compose.runtime:runtime-rxjava2"
    const val composeLifeCycle =  "androidx.lifecycle:lifecycle-runtime-compose:${Versions.composeLifeCycle}"
    // Integration with activities
    const val composeActivity = "androidx.activity:activity-compose:${Versions.composeActivity}"
    // Integration with ViewModels
    const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.composeViewModel}"
    /**
    Compose
     **/

    // Multidex
    const val multiDex = "androidx.multidex:multidex:${Versions.multiDex}"

    const val webSocket = "org.java-websocket:Java-WebSocket:${Versions.webSocket}"

    const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"

    const val customCrash = "cat.ereza:customactivityoncrash:${Versions.customCrash}"
}

object LibraryAndroidTesting {
    const val jUnit ="junit:junit:${Versions.junitVersion}"
    const val jUnitTest = "androidx.test.ext:junit:${Versions.testExtJunitVersion}"
    const val jUnitJupiterTest = "org.junit.jupiter:junit-jupiter:${Versions.jUnitJupiterVersion}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"

    const val roboElectric = "org.robolectric:robolectric:${Versions.roboElectric}"

    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
    const val testRunner =  "androidx.test:runner:${Versions.test}"
    const val testRules = "androidx.test:rules:${Versions.test}"

    // UI Tests Compose
    const val composeTestJunit4 = "androidx.compose.ui:ui-test-junit4"
    const val composeTestManifest = "androidx.compose.ui:ui-test-manifest"

    // For Robolectric tests.
    // Dagger Hilt
    const val dagger = "com.google.dagger:dagger-compiler:${Versions.daggerVersion}"
    const val daggerHiltTesting = "com.google.dagger:hilt-android-testing:${Versions.daggerVersion}"

    const val truth = "com.google.truth:truth:${Versions.truth}"

    const val mockitoCore =  "org.mockito:mockito-core:${Versions.mockito}"

    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"

    const val archCore = "androidx.arch.core:core-testing:${Versions.archCore}"
}