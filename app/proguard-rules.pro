# Jetpack Compose
-keepclassmembers class * extends androidx.compose.runtime.Composer { *; }
-keep class androidx.compose.runtime.Recomposer { *; }
-keep interface androidx.compose.runtime.Composer { *; }

# Room Database
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>(...);
}
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep class * extends androidx.room.Dao
-dontwarn androidx.room.paging.**

# Data Models (Keep models used for JSON parsing and database)
-keep class com.santelocale.data.entity.** { *; }
-keep class com.santelocale.data.FoodJsonWrapper { *; }
-keep class com.santelocale.data.FoodData { *; }

# Gson specific rules
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keep class com.google.gson.stream.** { *; }

# Coil (Image Loading)
-keep class coil.** { *; }
-dontwarn coil.**
-dontwarn okio.**

# DataStore
-keep class androidx.datastore.** { *; }

# WorkManager
-keep class androidx.work.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepclassmembernames class kotlinx.coroutines.android.HandlerContext$HandlerPost {
    private final java.lang.Runnable runnable;
}

# SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }
-keep class net.zetetic.** { *; }
-dontwarn net.sqlcipher.**
-dontwarn net.zetetic.**

# General optimizations
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
