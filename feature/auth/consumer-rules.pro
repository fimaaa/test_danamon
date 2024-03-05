# Keep data binding classes
-keep class androidx.databinding.** { *; }

# Keep generated data binding classes
-keep class id.general.feature.auth.databinding.** { *; }

# Keep the default constructor for FragmentLoginBinding
-keepclassmembers class id.general.feature.auth.databinding.* {
    <init>(android.view.View);
}

-keep class id.general.feature.auth.databinding.* {
    public static id.general.feature.auth.databinding.* inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}