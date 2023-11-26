package dev.veryniche.quickqr.snapshot

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
abstract class PaparazziShowkaseTest<T : ScreenshotPreview>(
    private val preview: T,
    config: TestConfig = TestConfig(Device.PIXEL_6, NightMode.NOTNIGHT, 1f),
) {

    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.5,
        showSystemUi = false,
        deviceConfig = when (config.device) {
            Device.PIXEL_6 -> DeviceConfig.PIXEL_6
//            Device.PIXEL_C -> DeviceConfig.PIXEL_C
        }.copy(
            nightMode = config.nightMode,
            fontScale = config.fontScale,
        ),
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun screenshotTest() {
        paparazzi.snapshot {
            preview.content.invoke()
        }
    }
}

interface ScreenshotPreview {
    val content: @Composable () -> Unit
}