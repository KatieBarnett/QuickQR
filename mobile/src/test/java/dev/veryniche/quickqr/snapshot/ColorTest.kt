package dev.veryniche.quickqr.snapshot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseBrowserColor
import dev.katiebarnett.welcometoflip.showkase.getMetadata
import org.junit.runners.Parameterized

class ColorPreview(
    private val colorBrowserColor: ShowkaseBrowserColor,
) : ScreenshotPreview {

    override val content: @Composable () -> Unit = {
        val backgroundColor = colorBrowserColor.color
        val textColor = if (backgroundColor.luminance() < 0.5) {
            Color.White
        } else {
            Color.Black
        }
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp).background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = colorBrowserColor.colorName,
                fontSize = 18.sp,
                color = textColor,
            )
        }
    }

    override fun toString(): String =
        "color=${colorBrowserColor.colorGroup}:${colorBrowserColor.colorName}"
}

class ColorTest(
    colorPreview: ColorPreview,
) : PaparazziShowkaseTest<ColorPreview>(colorPreview) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return Showkase.getMetadata().colorList.map { arrayOf(ColorPreview(it)) }
        }
    }
}