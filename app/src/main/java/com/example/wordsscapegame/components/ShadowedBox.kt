package com.example.wordsscapegame.components

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