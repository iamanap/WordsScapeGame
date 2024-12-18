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

@Composable
fun TextOutlinedAndFilled(
    text: String,
    color: Color = Color.White,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    strokeWidth: Float = 4f,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentSize()
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = style.copy(
                color = outlineColor,
                drawStyle = Stroke(
                    miter = 1f,
                    width = strokeWidth,
                    join = StrokeJoin.Round
                )
            ),
        )
        Text(
            modifier = modifier,
            text = text,
            style = style.copy(
                color = color
            )
        )
    }
}