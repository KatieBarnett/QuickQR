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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
}
-keep class * extends com.google.crypto.tink.shaded.protobuf.GeneratedMessageLite { *; }

# from Missing classes detected while running R8 message
-dontwarn dev.veryniche.quickqr.core.Constants
-dontwarn dev.veryniche.quickqr.core.ImageUtilKt
-dontwarn dev.veryniche.quickqr.core.di.DispatchersModule_ProvidesIODispatcherFactory
-dontwarn dev.veryniche.quickqr.core.model.QRCodeItem
-dontwarn dev.veryniche.quickqr.core.model.QRColor
-dontwarn dev.veryniche.quickqr.core.model.QRColorKt
-dontwarn dev.veryniche.quickqr.core.model.QRIcon
-dontwarn dev.veryniche.quickqr.core.model.QRIconKt
-dontwarn dev.veryniche.quickqr.core.theme.Dimen
-dontwarn dev.veryniche.quickqr.core.theme.ThemeKt
-dontwarn dev.veryniche.quickqr.storage.QRCodesDataSource
-dontwarn dev.veryniche.quickqr.storage.QRCodesRepository
-dontwarn dev.veryniche.quickqr.storage.QrCodesSerializer
-dontwarn dev.veryniche.quickqr.storage.di.DataStoreModule_ProvidesQRCodesStoreFactory
-dontwarn dev.veryniche.quickqr.storage.QRCodesSerializer
-dontwarn dev.veryniche.quickqr.storage.UserPreferences
-dontwarn dev.veryniche.quickqr.storage.UserPreferencesRepository
-dontwarn dev.veryniche.quickqr.storage.di.DataStoreModule_ProvidePreferencesDataStoreFactory