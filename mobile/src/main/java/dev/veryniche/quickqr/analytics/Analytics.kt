package dev.veryniche.quickqr.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import dev.veryniche.quickqr.analytics.Analytics.Screen.MainScreen
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import timber.log.Timber

object Analytics {
    object Screen {
        const val MainScreen = "Main_Screen"
        const val ExpandedCode = "Expanded_Code"
        const val About = "About"
    }

    object Action {
        const val AddCode = "Add_Code"
        const val EditCode = "Edit_Code"
        const val ExpandCode = "Expand_Code"
        const val DeleteCode = "Delete_Code"
        const val ScanCodeEdit = "Scan_Code_Edit"
        const val ScanCodeAdd = "Scan_Code_Add"
        const val EnterCodeManually = "Enter_Code_Manually_Add"
        const val WelcomeProVersion = "Welcome_Pro_version"
        const val AddMoreProVersion = "Add_More_Pro_version"
        const val AboutProVersion = "About_Pro_version"
        const val AdsClick = "Ads_Click"
        const val VisitContentDestination = "Visit_Content_Destination"
    }

    object Type {
        const val ColorChoice = "Color"
        const val IconChoice = "Icon"
    }
}

@Composable
fun TrackedScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit, // Send the 'started' analytics event
) {
    if (!LocalInspectionMode.current) {
        // Safely update the current lambdas when a new one is provided
        val currentOnStart by rememberUpdatedState(onStart)

        // If `lifecycleOwner` changes, dispose and reset the effect
        DisposableEffect(lifecycleOwner) {
            // Create an observer that triggers our remembered callbacks
            // for sending analytics events
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    currentOnStart()
                }
            }

            // Add the observer to the lifecycle
            lifecycleOwner.lifecycle.addObserver(observer)

            // When the effect leaves the Composition, remove the observer
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}

internal fun trackScreenView(name: String?) {
    Timber.d("Track screen: $name")
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        param(FirebaseAnalytics.Param.SCREEN_NAME, name ?: "Unknown")
    }
}

internal fun trackMainScreenView(tileCount: Int) {
    Timber.d("Track screen: $MainScreen")
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        param(FirebaseAnalytics.Param.SCREEN_NAME, MainScreen)
        param(FirebaseAnalytics.Param.ITEMS, tileCount.toString())
    }
}

fun trackAction(action: String, isProVersion: Boolean? = null) {
    Timber.d("Track action: $action")
    Firebase.analytics.logEvent(action) {
        isProVersion?.let {
            param(
                FirebaseAnalytics.Param.CAMPAIGN,
                if (isProVersion) {
                    "Pro_Version"
                } else {
                    "Free_Version"
                }
            )
        }
    }
}

fun trackColorChoice(color: QRColor) {
    Timber.d("Track color choice: ${color.name}")
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
        param(FirebaseAnalytics.Param.ITEM_NAME, color.name)
        param(FirebaseAnalytics.Param.CONTENT_TYPE, Analytics.Type.ColorChoice)
    }
}
fun trackIconChoice(icon: QRIcon) {
    Timber.d("Track icon choice: ${icon.name}")
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
        param(FirebaseAnalytics.Param.ITEM_NAME, icon.name)
        param(FirebaseAnalytics.Param.CONTENT_TYPE, Analytics.Type.IconChoice)
    }
}
