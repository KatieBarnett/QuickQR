package dev.veryniche.quickqr.util

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.theme.Dimen

@Composable
fun AboutAppText(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        append(stringResource(id = R.string.about_instructions))
    }
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "bgg_wttm_link", start = offset, end = offset)
                .firstOrNull()?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(intent)
            }
        },
        modifier = modifier
    )
}

@Composable
fun RemoveAdsText(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val annotatedString = buildAnnotatedString {
        append(stringResource(id = R.string.welcome_remove_ads_1))
        append(" ")
        pushStringAnnotation(tag = "here", annotation = stringResource(id = R.string.welcome_remove_ads_2))
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline)) {
            append(stringResource(id = R.string.welcome_remove_ads_2))
        }
        append(" ")
        append(stringResource(id = R.string.welcome_remove_ads_3))
    }
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "here", start = offset, end = offset)
                .firstOrNull()?.let {
                    onClick.invoke()
                }
        },
        modifier = modifier
    )
}

@Composable
fun UnorderedListText(textLines: List<Int>, modifier: Modifier = Modifier) {
    val bullet = "\u2022"
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = Dimen.bulletTextIndent))
    Text(
        buildAnnotatedString {
            textLines.forEach {
                val string= stringResource(id = it)
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