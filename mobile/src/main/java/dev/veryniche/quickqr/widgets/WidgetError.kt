package dev.veryniche.quickqr.widgets

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.theme.Dimen

@Composable
fun WidgetError(
    useDarkTextOnBackground: Boolean,
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = context.getString(R.string.widget_error),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = if (useDarkTextOnBackground) {
                        ColorProvider(Color.Black)
                    } else {
                        ColorProvider(Color.White)
                    }
                ),
                modifier = GlanceModifier
            )
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(heightDp = 120, widthDp = 120)
@Preview(heightDp = 180, widthDp = 120)
@Composable
fun WidgetErrorPreview() {
    GlanceTheme(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            GlanceTheme.colors
        } else {
            QuickQRGlanceColorScheme.colors
        }
    ) {
        Box(GlanceModifier.padding(Dimen.spacing)) {
            WidgetError(useDarkTextOnBackground = false)
        }
    }
}
