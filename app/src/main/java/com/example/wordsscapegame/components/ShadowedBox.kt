package com.example.wordsscapegame.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShadowedBox(
    backgroundColor: Color,
    shadowColor: Color,
    roundedCornerShape: RoundedCornerShape,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 59.dp, height = 40.dp)
            .wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = roundedCornerShape)
                .clip(shape = roundedCornerShape)
                .background(color = shadowColor)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = 59.dp, height = 36.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = roundedCornerShape)
                .clip(shape = roundedCornerShape)
                .background(color = backgroundColor)
        ) {
            content()
        }
    }
}