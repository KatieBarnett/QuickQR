package dev.veryniche.quickqr.core.util

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

fun @receiver:ColorInt Int.isDark(): Boolean =
    ColorUtils.calculateLuminance(this) < 0.5