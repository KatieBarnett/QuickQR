package dev.veryniche.quickqr.widgets

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius

fun GlanceModifier.adaptiveCornerRadius(): GlanceModifier {
    return if (VERSION.SDK_INT >= VERSION_CODES.S) {
        this.cornerRadius(android.R.dimen.system_app_widget_inner_radius)
    } else {
        this.cornerRadius(2.dp)
    }
}