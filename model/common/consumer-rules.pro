# Preserve annotations
-keepattributes *Annotation*

# Preserve fields annotated with @SerializedName
-keepclassmembers class com.general.model.common.* {
    @com.google.gson.annotations.SerializedName *;
}

-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class org.apache.commons.** { *; }
-keep class org.joda.time.** { *; }
-keep class javax.** { *; }
-keep class kotlin.** { *; }
-keep class com.general.model.common.** { *; }
-keep class com.general.model.common.** { *; }
-keep class com.general.model.common.** { *; }

-dontwarn javax.**
-dontwarn kotlin.**
-dontwarn sun.misc.**

-keep class com.general.model.common.ErrorResponse {
    <fields>;
    <init>(android.os.Parcel);
}

-keep class com.general.model.common.ErrorResponse$CREATOR {
    *;
}

-keepnames class com.general.model.common.ErrorResponse {
    ** CREATOR;
}