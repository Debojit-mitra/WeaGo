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

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public class * extends com.bumptech.glide.annotation.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
#-keep class com.bumptech.glide.** { *; }

# OkHttp3
#-dontwarn okhttp3.**
#-keep class okhttp3.** { *; }
#-keep interface okhttp3.** { *; }

# Gson
#-keep class com.google.gson.** { *; }

#-keepclassmembers class * {
 #   @com.google.gson.annotations.SerializedName <fields>;
#}

#Models
-keepclassmembers class com.bunny.weather.weago.models.** {
    *;
}

# General Rules
-keepattributes Signature
-keepattributes *Annotation*

