// Top-level build file where you can add configuration options common to all sub-projects/modules.
//
// VERSION ALIGNMENT — Critical: Kotlin, KSP and Compose Compiler must all match.
//
//  Kotlin:          2.0.21
//  KSP:             2.0.21-1.0.28   (format: {kotlin_version}-{ksp_version})
//  Compose Plugin:  2.0.21          (must equal Kotlin version exactly)
//  AGP:             8.3.2
//  Hilt:            2.51.1
//
plugins {
    id("com.android.application")          version "8.3.2"     apply false
    id("org.jetbrains.kotlin.android")     version "2.0.21"    apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    id("com.google.dagger.hilt.android")   version "2.51.1"    apply false
    id("com.google.gms.google-services")   version "4.4.2"     apply false
    id("com.google.devtools.ksp")          version "2.0.21-1.0.28" apply false
}
