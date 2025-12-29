# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the Android SDK.

# Keep Room classes
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Coil (Image Loading)
-keep class coil.** { *; }
-dontwarn coil.**

# WorkManager (Reminders)
-keep class androidx.work.** { *; }

# Gson (JSON Parsing)
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keep class com.santelocale.data.** { *; } # Keep data models used with Gson

# Kotlin Metadata
-keep class kotlin.Metadata { *; }
