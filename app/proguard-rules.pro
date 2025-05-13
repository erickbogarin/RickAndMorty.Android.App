# --- Retrofit + Gson ---
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Retrofit: preserve model classes with Gson annotations
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepattributes Signature
-keepattributes *Annotation*

# RxJava
-dontwarn sun.misc.**
-dontwarn io.reactivex.**
-keepclassmembers class rx.internal.util.unsafe.* {
    long p*;
}

# Para Gson
-keep class com.example.rickandmorty.**.model.** { *; }
-keepclassmembers class com.example.rickandmorty.**.model.** {
    <fields>;
}
-keepattributes Signature
-keepattributes *Annotation*

# --- AndroidX Lifecycle (ViewModel, LiveData) ---
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class androidx.lifecycle.ViewModel
-keep class androidx.lifecycle.MutableLiveData
-keep class androidx.lifecycle.LiveData

# --- Navigation Component ---
-keepclassmembers class * {
    @androidx.navigation.NavArgs <methods>;
}
-keep class ** implements androidx.navigation.NavArgs

# --- ViewBinding / DataBinding ---
-keep class **.databinding.*Binding { *; }
-keep class **.databinding.* { *; }
-keepclassmembers class * extends androidx.databinding.ViewDataBinding {
    public static <fields>;
    public static <methods>;
}

# --- Logging ---
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# -dontoptimize
