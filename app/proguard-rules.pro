# ═══════════════════════════════════════════════════════
# Nabha Sehat — ProGuard Rules
# All dependencies below are FREE / open-source
# ═══════════════════════════════════════════════════════

# Firebase (FREE Spark plan)
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**

# Hilt (FREE - Apache 2.0)
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-dontwarn dagger.hilt.**

# OkHttp (FREE - Apache 2.0)
-dontwarn okhttp3.**
-dontwarn okio.**

# Retrofit (FREE - Apache 2.0)
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Room (FREE - Apache 2.0)
-keep class androidx.room.** { *; }

# Jitsi Meet SDK (FREE - Apache 2.0 / open-source)
-keep class org.jitsi.** { *; }
-keep class org.webrtc.** { *; }
-dontwarn org.jitsi.**
-dontwarn org.webrtc.**

# OSMDroid — OpenStreetMap (FREE - Apache 2.0)
-keep class org.osmdroid.** { *; }
-dontwarn org.osmdroid.**

# Coil (FREE - Apache 2.0)
-dontwarn coil.**

# Kotlin Serialization
-keepattributes *Annotation*
-keepclassmembers class kotlinx.serialization.json.** { *** *; }

# App Domain Models
-keep class com.nabha.telemedicine.domain.** { *; }
-keep class com.nabha.telemedicine.data.** { *; }

# BuildConfig
-keep class com.nabha.telemedicine.BuildConfig { *; }
