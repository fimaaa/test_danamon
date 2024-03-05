# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
### GSON ###

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
# -keep class mypersonalclass.data.model.** { *; }

#### Databinding ####
#-keep class * extends androidx.databinding.DataBinderMapper { *; }
#-keep class com.baseapp.daggerhilt.DataBinderMapperImpl { *; }
#-keep class com.basedagger.common.DataBinderMapperImpl { *; }
#-keep class com.feature.baseapp.firebasetest.DataBinderMapperImpl { *; }
#-keep class com.baseapp.daggerhilt.databinding.*
#-keep class com.basedagger.common.databinding.*
#-keep class com.feature.baseapp.firebasetest.*
# Keep data binding classes
-keep class androidx.databinding.** { *; }

# Keep generated data binding classes
-keep class com.general.testdanamon.databinding.** { *; }

### ###
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

-keepattributes *Annotation*
-keepclassmembers enum androidx.lifecycle.Lifecycle$Event {
    <fields>;
}
-keep !interface * implements androidx.lifecycle.LifecycleObserver {
}
-keep class * implements androidx.lifecycle.GeneratedAdapter {
    <init>(...);
}
-keepclassmembers class ** {
    @androidx.lifecycle.OnLifecycleEvent *;
}
# this rule is need to work properly when app is compiled with api 28, see b/142778206
# Also this rule prevents registerIn from being inlined.
-keepclassmembers class androidx.lifecycle.ReportFragment$LifecycleCallbacks { *; }

##GoogleMaterial###
#-keep class com.google.android.material.** { *; }
#-keep interface com.google.android.material.** { *; }

-keep class com.google.firebase.crashlytics.** { *; }
