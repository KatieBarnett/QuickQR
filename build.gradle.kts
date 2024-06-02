// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.dagger.hilt.android) apply (false)
    alias(libs.plugins.ksp) apply (false)
    alias(libs.plugins.protobuf) apply (false)
    alias(libs.plugins.firebase.crashlytics) apply (false)
    alias(libs.plugins.google.services) apply (false)
    alias(libs.plugins.paparazzi) apply (false)
    alias(libs.plugins.compose.compiler) apply (false)
    alias(libs.plugins.kotlin.serialization) apply (false)
}

ext {
    extra["appVersionName"] = "2.1.0"
    extra["appVersionCode"] = 42
    extra["compileSdk"] = 34
    extra["targetSdk"] = 34
    extra["minSdk"] = 26
    extra["wearMinSdk"] = 30
}

true // Needed to make the Suppress annotation work for the plugins block