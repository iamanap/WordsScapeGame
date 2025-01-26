package com.example.wordsscapegame.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

/**
 * A composable function that displays text with an outline and fill.
 *
 * This function creates a text element with a specified outline color and fill color.
 * It achieves this by drawing the text twice: once with the outline color and a stroke,
 * and then again with the fill color. This creates the effect of outlined and filled text.
 *
 * @param modifier Modifier to be applied to the text layout.
 * @param text The text to be displayed.
 * @param color The fill color of the text.
 * @param outlineColor The color of the text outline.
 * @param strokeWidth The width of the outline stroke.
 * @param style The text style to be applied.
 */
@Composable
fun TextOutlinedAndFilled(
    modifier: Modifier = Modifier,
    text: String = "",
    color: Color = Color.White,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    strokeWidth: Float = 4f,
    style: TextStyle =MaterialTheme.typography.labelMedium
) {
    Box(
        modifier = modifier
            .wrapContentSize()
    ) {
        CustomText(
            modifier = modifier,
            text = text,
            style = style.copy(
                color = outlineColor,
                drawStyle = Stroke(
                    miter = 1f,
                    width = strokeWidth,
                    join = StrokeJoin.Round
                )
            )
        )
        CustomText(
            modifier = modifier,
            text = text,
            style = style.copy(
                color = color
            )
        )
    }
}

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        textAlign = TextAlign.Center,
        maxLines = 1
    )
}

@Preview(showBackground = true)
@Composable
fun TextOutlinedAndFilledPreview() {
    TextOutlinedAndFilled()

}