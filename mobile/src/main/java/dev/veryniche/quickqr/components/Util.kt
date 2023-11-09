package dev.veryniche.quickqr.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f
@Composable
fun ResponsiveText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    textAlign: TextAlign,
    textColor: Color,
) {
    var textSize: TextUnit by remember{ mutableStateOf(textStyle.fontSize) }
    Text(
        text = text,
        color = textColor,
        textAlign = textAlign,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.hasVisualOverflow) {
                textSize *= TEXT_SCALE_REDUCTION_INTERVAL
            }
        },
        style = textStyle.copy(fontSize = textSize),
        modifier = modifier
    )
}