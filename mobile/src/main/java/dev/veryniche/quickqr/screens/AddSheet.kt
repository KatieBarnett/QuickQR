package dev.veryniche.quickqr.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import dev.veryniche.quickqr.ScannedCode
import dev.veryniche.quickqr.components.AddChoice
import dev.veryniche.quickqr.components.AddEnterName
import dev.veryniche.quickqr.components.AddEnterUrl
import dev.veryniche.quickqr.components.AddSelectColour
import dev.veryniche.quickqr.components.AddSelectIcon
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewComponent
import dev.veryniche.quickqr.util.Analytics
import dev.veryniche.quickqr.util.trackAction
import dev.veryniche.quickqr.util.trackColorChoice
import dev.veryniche.quickqr.util.trackIconChoice
import kotlinx.coroutines.launch

enum class AddPage {
    ADD_CHOICE, ENTER_URL, SELECT_COLOR, SELECT_ICON, ENTER_NAME
}

@OptIn(ExperimentalFoundationApi::class)
suspend fun PagerState.scrollToNextPage() {
    if (canScrollForward) {
        this.animateScrollToPage(currentPage + 1)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddSheet(
    modifier: Modifier,
    scannedCode: ScannedCode?,
    onSaveClick: (name: String?, content: String?, icon: QRIcon?, primaryColor: QRColor?) -> ImageBitmap?,
    onScanClick: () -> Unit,
) {
    val pages by remember {
        mutableStateOf(
            listOf(
                AddPage.ADD_CHOICE,
                AddPage.ENTER_URL,
                AddPage.SELECT_COLOR,
                AddPage.SELECT_ICON,
                AddPage.ENTER_NAME
            )
        )
    }
    var name by remember { mutableStateOf<String?>(null) }
    var content by remember { mutableStateOf<String?>(null) }
    var icon by remember { mutableStateOf<QRIcon?>(null) }
    var primaryColor by remember { mutableStateOf<QRColor?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { pages.size })

    LaunchedEffect(key1 = scannedCode) {
        if (scannedCode != null && pagerState.currentPage == pages.indexOf(AddPage.ADD_CHOICE)) {
            pagerState.animateScrollToPage(pages.indexOf(AddPage.SELECT_COLOR))
            content = scannedCode.content
        }
    }

    HorizontalPager(
        state = pagerState,
        beyondBoundsPageCount = pagerState.pageCount,
        userScrollEnabled = false,
        modifier = modifier
    ) { page ->
        when (pages[page]) {
            AddPage.ADD_CHOICE -> {
                AddChoice(
                    onScanClick = {
                        onScanClick.invoke()
                        trackAction(Analytics.Action.ScanCodeAdd)
                    },
                    onEnterUrlClick = {
                        trackAction(Analytics.Action.EnterCodeManually)
                        coroutineScope.launch {
                            pagerState.scrollToNextPage()
                        }
                    },
                    modifier = Modifier
                )
            }
            AddPage.ENTER_URL -> {
                AddEnterUrl(
                    onNextClick = {
                        content = it
                        coroutineScope.launch {
                            pagerState.scrollToNextPage()
                        }
                    },
                    modifier = Modifier
                )
            }
            AddPage.SELECT_COLOR -> {
                AddSelectColour(
                    onColorClick = {
                        primaryColor = it
                        trackColorChoice(it)
                        coroutineScope.launch {
                            pagerState.scrollToNextPage()
                        }
                    },
                    modifier = Modifier
                )
            }
            AddPage.SELECT_ICON -> {
                AddSelectIcon(
                    onIconClick = {
                        icon = it
                        trackIconChoice(it)
                        coroutineScope.launch {
                            pagerState.scrollToNextPage()
                        }
                    },
                    modifier = Modifier
                )
            }
            AddPage.ENTER_NAME -> {
                AddEnterName(
                    onSaveClick = {
                        name = it
                        onSaveClick.invoke(name, content, icon, primaryColor)
                    },
                    modifier = Modifier
                )
            }
        }
    }
}

@PreviewComponent
@Composable
fun AddSheetPreview() {
    QuickQRTheme {
        Surface {
            AddSheet(
                scannedCode = null,
                modifier = Modifier,
                onSaveClick = { name, content, icon, primaryColor ->
                    null
                },
                onScanClick = {
                },
            )
        }
    }
}
