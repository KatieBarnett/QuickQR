@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "dev.veryniche.quickqr"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        applicationId = "dev.veryniche.quickqr"
        minSdk = rootProject.extra["wearMinSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
        versionCode = rootProject.extra["appVersionCode"] as Int
        versionName = rootProject.extra["appVersionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            versionNameSuffix = ".debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.material3.windowsizeclass)
    implementation(libs.material3.adaptive)
    implementation(libs.play.core.ktx)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.animation)
    implementation(libs.compose.runtime.livedata)

    implementation(libs.activity.ktx)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.compose.navigation)

    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.barcode.scanning)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.splashscreen)

    implementation(libs.timber)

    implementation(libs.play.services.code.scanner)
    implementation(libs.play.services.base)
    implementation(libs.play.services.ads)
    implementation(libs.qrgenerator)

    implementation(libs.billing)

    implementation(libs.datastore.preferences)
//
//    implementation(libs.play.app.update)
//    implementation(libs.play.app.update.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
//    wearApp(project(":wear"))
    implementation(project(":core"))
    implementation(project(":storage"))
    implementation(libs.showkase)
    ksp(libs.showkase.processor)
}