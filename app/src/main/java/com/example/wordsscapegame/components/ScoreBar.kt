package com.example.wordsscapegame.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wordsscapegame.R
import com.example.wordsscapegame.ui.theme.Typography
import com.example.wordsscapegame.ui.theme.WordsScapeGameTheme

@Composable
fun GameScoreBar(
    modifier: Modifier = Modifier,
    caughtScore: Int = 0,
    lostScore: Int = 0
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val boxModifier = Modifier
            .size(width = 59.dp, height = 40.dp)
        ShadowedBox(
            backgroundColor = WordsScapeGameTheme.extraColors.redContainerBackground,
            shadowColor = WordsScapeGameTheme.extraColors.redContainerShadowBackground,
            roundedCornerShape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
            content = { ScoreBoxContent(
                caughtScore = caughtScore,
                lostScore = lostScore,
                scoreType = ScoreType.Wrong
            ) },
            modifier = boxModifier
        )
        ShadowedBox(
            backgroundColor = WordsScapeGameTheme.extraColors.greenContainerBackground,
            shadowColor = WordsScapeGameTheme.extraColors.rightScoreContainerShadowBackground,
            roundedCornerShape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
            content = { ScoreBoxContent(
                caughtScore = caughtScore,
                lostScore = lostScore,
                scoreType = ScoreType.Right
            ) },
            modifier = boxModifier
        )
    }
}

@Composable
private fun ScoreBoxContent(
    scoreType: ScoreType,
    caughtScore: Int = 0,
    lostScore: Int = 0
) {

    val icon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(scoreType.icon),
            contentDescription = scoreType.text,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(scoreType.iconSize)
        )
    }
    val text: @Composable (text: String) -> Unit = { text ->
        TextOutlinedAndFilled(
            text = text,
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
            text("$lostScore")
        } else {
            text("$caughtScore")
            icon()
        }
    }
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