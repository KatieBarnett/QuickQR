package dev.veryniche.quickqr.snapshot

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseBrowserTypography
import dev.katiebarnett.welcometoflip.showkase.getMetadata
import org.junit.runners.Parameterized
import java.util.Locale

class TypographyPreview(
    private val showkaseBrowserTypography: ShowkaseBrowserTypography,
) : ScreenshotPreview {

    override val content: @Composable () -> Unit = {
        Text(
            text = showkaseBrowserTypography.typographyName.replaceFirstChar {
                it.titlecase(Locale.getDefault())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = showkaseBrowserTypography.textStyle,
        )
    }

    override fun toString(): String =
        "typography=${showkaseBrowserTypography.typographyGroup}:${showkaseBrowserTypography.typographyName}"
}

class TypographyTest(
    typographyPreview: TypographyPreview,
) : PaparazziShowkaseTest<TypographyPreview>(typographyPreview) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return Showkase.getMetadata().typographyList.map { arrayOf(TypographyPreview(it)) }
        }
    }
}