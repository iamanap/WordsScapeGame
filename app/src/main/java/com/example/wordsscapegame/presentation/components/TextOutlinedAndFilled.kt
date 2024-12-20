package com.example.wordsscapegame.components

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

@Composable
fun TextOutlinedAndFilled(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.White,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    strokeWidth: Float = 4f,
    style: TextStyle
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