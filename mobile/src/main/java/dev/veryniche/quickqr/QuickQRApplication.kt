package dev.veryniche.quickqr

import android.app.Application
import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree
import timber.log.Timber.Tree


@HiltAndroidApp
class QuickQRApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            if (priority == Log.ERROR) {
                Firebase.crashlytics.recordException(t ?: RuntimeException(message))
            } else if (priority == Log.WARN) {
                Firebase.crashlytics.log(message)
            }
        }
    }

}