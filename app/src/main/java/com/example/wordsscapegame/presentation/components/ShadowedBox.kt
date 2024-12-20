package com.example.wordsscapegame.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A composable function that creates a box with a shadow effect.
 *
 * This function creates a box with a background color and a shadow. The shadow is created by
 * drawing a slightly larger box behind the main box with a darker color. The shadow can be
 * customized using the `shadowColor`, `borderWidth`, and `elevation` parameters.
 *
 * @param modifier Modifier to be applied to the box.
 * @param backgroundColor The background color of the box.
 * @param shadowColor The color of the shadow.
 * @param borderWidth The width of the border around the box.
 * @param elevation The elevation of the box, which affects the size and intensity of the shadow.
 * @param roundedCornerShape The shape of the corners of the box.
 * @param content The content to be displayed inside the box.
 */
@Composable
fun ShadowedBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    shadowColor: Color,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 4.dp,
    roundedCornerShape: RoundedCornerShape,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(width = borderWidth, color = MaterialTheme.colorScheme.outline, shape = roundedCornerShape)
                .clip(shape = roundedCornerShape)
                .background(color = shadowColor)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = elevation)
                .border(width = borderWidth, color = MaterialTheme.colorScheme.outline, shape = roundedCornerShape)
                .clip(shape = roundedCornerShape)
                .background(color = backgroundColor)
        ) {
            content()
        }
    }
}