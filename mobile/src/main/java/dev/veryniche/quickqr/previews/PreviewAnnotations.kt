package dev.veryniche.quickqr.previews

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "small font", group = "Small Font", fontScale = 0.85f, widthDp = 137)
@Preview(name = "regular font", group = "Regular Font", fontScale = 1.0f, widthDp = 137)
@Preview(name = "large font", group = "Large Font", fontScale = 1.15f, widthDp = 137)
@Preview(name = "extra large font", group = "Extra Large Font", fontScale = 1.3f, widthDp = 137)
annotation class PreviewTile


@Preview(name = "small font", group = "Small Font", fontScale = 0.85f)
@Preview(name = "regular font", group = "Regular Font", fontScale = 1.0f)
@Preview(name = "large font", group = "Large Font", fontScale = 1.15f)
@Preview(name = "extra large font", group = "Extra Large Font", fontScale = 1.3f)
annotation class PreviewComponent

@Preview(device = Devices.PIXEL_3, group = "Regular Font", fontScale = 1.0f, showSystemUi = true)
@Preview(device = Devices.PIXEL_3, group = "Extra Large Font", fontScale = 1.3f, showSystemUi = true)
@Preview(device = Devices.PIXEL_4_XL, group = "Regular Font", fontScale = 1.0f, showSystemUi = true)
@Preview(device = Devices.PIXEL_4_XL, group = "Extra Large Font", fontScale = 1.3f, showSystemUi = true)
annotation class PreviewScreen
