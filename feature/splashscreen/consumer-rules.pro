# Keep data binding classes
-keep class androidx.databinding.** { *; }

# Keep generated data binding classes
-keep class com.general.splashscreen.databinding.** { *; }

# Keep the default constructor for FragmentLoginBinding
-keepclassmembers class com.general.splashscreen.databinding.* {
    <init>(android.view.View);
}

-keep class com.general.splashscreen.databinding.* {
    public static com.general.splashscreen.databinding.* inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}