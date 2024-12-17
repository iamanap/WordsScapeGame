package com.example.wordsscapegame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wordsscapegame.components.ShadowedBox
import com.example.wordsscapegame.components.TextOutlinedAndFilled
import com.example.wordsscapegame.ui.theme.Typography
import com.example.wordsscapegame.ui.theme.WordsScapeGameTheme

@Composable
fun GameScreen(
    onStartResetClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 26.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameScoreBar()
    }
}

@Composable
private fun GameScoreBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ShadowedBox(
            backgroundColor = WordsScapeGameTheme.extraColors.incorrectScoreContainerBackground,
            shadowColor = WordsScapeGameTheme.extraColors.incorrectScoreContainerShadowBackground,
            roundedCornerShape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
            content = { ScoreBoxContent(ScoreType.Wrong) }
        )
        ShadowedBox(
            backgroundColor = WordsScapeGameTheme.extraColors.rightScoreContainerBackground,
            shadowColor = WordsScapeGameTheme.extraColors.rightScoreContainerShadowBackground,
            roundedCornerShape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
            content = { ScoreBoxContent(ScoreType.Right) }
        )
    }
}

@Composable
private fun ScoreBoxContent(scoreType: ScoreType) {

    val icon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(scoreType.icon),
            contentDescription = scoreType.text,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(scoreType.iconSize)
        )
    }
    val text: @Composable () -> Unit = {
        TextOutlinedAndFilled(
            text = "0",
            color = Color.White,
            outlineColor = MaterialTheme.colorScheme.outline,
            style = Typography.labelSmall,
            modifier = Modifier
                .padding(start = 4.dp)
        )
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(scoreType.padding),
    ) {
        if (scoreType == ScoreType.Wrong) {
            icon()
            text()
        } else {
            text()
            icon()
        }
    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    GameScreen()
}

private enum class ScoreType(
    val text: String,
    val icon: Int,
    val iconSize: Dp,
    val padding: Modifier
) {
    Wrong(
        text = "Wrong Number",
        icon = R.drawable.ic_close_filled_outline,
        iconSize = 14.dp,
        padding = Modifier.padding(end = 4.dp)
    ),
    Right(
        text = "Right Number",
        icon = R.drawable.ic_check_filled_outlined,
        iconSize = 22.dp,
        padding = Modifier.padding(start = 0.dp)
    )
}