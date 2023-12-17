package dev.veryniche.quickqr.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import dev.veryniche.quickqr.components.AddChoice
import dev.veryniche.quickqr.components.AddEnterName
import dev.veryniche.quickqr.components.AddEnterUrl
import dev.veryniche.quickqr.components.AddSelectColour
import dev.veryniche.quickqr.components.AddSelectIcon
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewComponent
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
    onSaveClick: (
        name: String?,
        content: String?,
        icon: QRIcon?,
        primaryColor: QRColor?,
    ) -> ImageBitmap?,
    onScanClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    var pages by remember {
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
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var name by remember { mutableStateOf<String?>(null) }
    var content by remember { mutableStateOf<String?>(null) }
    var icon by remember { mutableStateOf<QRIcon?>(null) }
    var primaryColor by remember { mutableStateOf<QRColor?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    HorizontalPager(
        state = pagerState,
        beyondBoundsPageCount = pagerState.pageCount,
        userScrollEnabled = false,
        modifier = modifier
    ) { page ->
        when (pages.get(page)) {
            AddPage.ADD_CHOICE -> {
                AddChoice(
                    onScanClick = {
                        onScanClick.invoke()
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pages.indexOf(AddPage.SELECT_COLOR))
                        }
                    },
                    onEnterUrlClick = {
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
                        onSaveClick.invoke(name, content, icon, primaryColor)?.let {
                            imageBitmap = it
                        }
                    },
                    modifier = Modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewComponent
@Composable
fun AddSheetPreview() {
    QuickQRTheme {
        Surface {
            AddSheet(
                modifier = Modifier,
                onSaveClick = { name, content, icon, primaryColor ->
                    null
                },
                { }
            ) {
            }
        }
    }
}
