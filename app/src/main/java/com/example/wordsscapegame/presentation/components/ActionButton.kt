package com.example.wordsscapegame.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wordsscapegame.ui.screens.GameStatus
import com.example.wordsscapegame.presentation.theme.WordsScapeGameTheme

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    shadowColor: Color = WordsScapeGameTheme.extraColors.greenContainerShadowBackground,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    onClicked: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "actionButtonScaleAnimation"
    )

    ShadowedBox(
        backgroundColor = backgroundColor,
        shadowColor = shadowColor,
        borderWidth = 2.dp,
        roundedCornerShape = RoundedCornerShape(14.dp),
        elevation = 6.dp,
        modifier = modifier
            .size(width = 200.dp, height = 88.dp)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    pressed = true
                    tryAwaitRelease()
                    pressed = false
                    onClicked()
                })
            }
    ) {
        TextOutlinedAndFilled(
            text = text,
            style = textStyle,
            strokeWidth = 8f,
            modifier = Modifier
                .wrapContentSize()
        )
    }
}

@Preview
@Composable
private fun ActionButtonPreview() {
    ActionButton(
        text = stringResource(GameStatus.ReadyToPlay.text),
        onClicked = {}
    )
}