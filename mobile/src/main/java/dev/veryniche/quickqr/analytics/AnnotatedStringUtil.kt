package dev.veryniche.quickqr.analytics

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import dev.veryniche.quickqr.core.theme.Dimen

@Composable
fun UnorderedListText(textLines: List<Int>, modifier: Modifier = Modifier) {
    val bullet = "\u2022"
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = Dimen.bulletTextIndent))
    Text(
        buildAnnotatedString {
            textLines.forEach {
                val string = stringResource(id = it)
                withStyle(style = paragraphStyle) {
                    append(bullet)
                    append("\t")
                    append(string)
                }
            }
        },
        modifier = modifier
    )
}
