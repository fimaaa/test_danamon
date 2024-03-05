# Keep data binding classes
-keep class androidx.databinding.** { *; }

# Keep data binding classes
-keep class com.general.common.databinding.** { *; }

# Keep the default constructor for FragmentLoginBinding
-keepclassmembers class com.general.common.databinding.* {
    <init>(android.view.View);
}

-keep class com.general.common.databinding.* {
    public static com.general.common.databinding.* inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}
#-keep class com.basedagger.common.customview.* { *; }
#-keep class com.google.android.material.** { *; }
#-keep interface com.google.android.material.** { *; }