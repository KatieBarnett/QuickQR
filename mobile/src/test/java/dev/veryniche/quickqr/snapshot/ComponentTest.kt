package dev.veryniche.quickqr.snapshot

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.android.resources.NightMode
import dev.katiebarnett.welcometoflip.showkase.getMetadata
import org.junit.runners.Parameterized

class ComponentPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent,
) : ScreenshotPreview {
    override val content: @Composable () -> Unit = showkaseBrowserComponent.component
    override fun toString(): String = "component=${
        listOfNotNull(
            showkaseBrowserComponent.group,
            showkaseBrowserComponent.componentName,
            showkaseBrowserComponent.styleName,
        ).joinToString(":")
    }"
}

class ComponentTest(
    componentPreview: ComponentPreview,
    config: TestConfig,
) : PaparazziShowkaseTest<ComponentPreview>(componentPreview, config) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}, {1}")
        fun data(): Collection<Array<Any>> {
            val componentPreviews = Showkase.getMetadata().componentList.map(::ComponentPreview)
            val fontScales = listOf(1f/*, 1.5f*/)
            val modes = listOf(/*NightMode.NIGHT, */NightMode.NOTNIGHT)

            return componentPreviews.flatMap { componentPreview ->
                fontScales.flatMap { fontScale ->
                    modes.mapNotNull { mode ->
                        if (mode == NightMode.NIGHT && fontScale == 1.5f) {
                            null
                        } else {
                            arrayOf(componentPreview, TestConfig(Device.PIXEL_6, mode, fontScale))
                        }
                    }
                }
            }
        }
    }
}