package com.example.wordsscapegame.presentation.components

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
import com.example.wordsscapegame.presentation.theme.DarkGreen
import com.example.wordsscapegame.presentation.theme.DarkRed
import com.example.wordsscapegame.presentation.theme.LightGreen
import com.example.wordsscapegame.presentation.theme.Red
import com.example.wordsscapegame.presentation.theme.Typography

/**
 * A composable function that displays the score bar in the game.
 *
 * This function displays two score boxes: one for the caught score and one for the lost score.
 * The score boxes are arranged horizontally and spaced evenly.
 *
 * @param modifier Modifier to be applied to the score bar layout.
 * @param caughtScore The current score for caught words.
 * @param lostScore The current score for lost words.
 */
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
        ScoreBox(
            boxModifier = Modifier
                .size(width = 59.dp, height = 40.dp),
            caughtScore = caughtScore,
            lostScore = lostScore,
            scoreType = ScoreType.Lost
        )
        ScoreBox(
            boxModifier = Modifier
                .size(width = 59.dp, height = 40.dp),
            caughtScore = caughtScore,
            lostScore = lostScore,
            scoreType = ScoreType.Caught
        )
    }
}

/**
 * A composable function that displays a single score box.
 *
 * This function displays the score value along with an icon representing the score type
 * (caught or lost). The score box has a background color, shadow, and rounded corners.
 *
 * @param boxModifier Modifier to be applied to the score box layout.
 * @param caughtScore The current score for caught words.
 * @param lostScore The current score for lost words.
 * @param scoreType The type of score to display (caught or lost).
 */
@Composable
private fun ScoreBox(
    boxModifier: Modifier,
    caughtScore: Int,
    lostScore: Int,
    scoreType: ScoreType
) {
    ShadowedBox(
        backgroundColor = scoreType.backgroundColor,
        shadowColor = scoreType.shadowColor,
        roundedCornerShape = scoreType.roundedCornerShape,
        content = {
            ScoreBoxContent(
                caughtScore = caughtScore,
                lostScore = lostScore,
                scoreType = scoreType
            )
        },
        modifier = boxModifier
    )
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
        if (scoreType == ScoreType.Lost) {
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
    val padding: Modifier,
    val backgroundColor: Color,
    val shadowColor: Color,
    val roundedCornerShape: RoundedCornerShape
) {
    Lost(
        text = "Wrong Number",
        icon = R.drawable.ic_close_filled_outline,
        iconSize = 14.dp,
        padding = Modifier.padding(end = 4.dp),
        backgroundColor = Red,
        shadowColor = DarkRed,
        roundedCornerShape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
    ),
    Caught(
        text = "Right Number",
        icon = R.drawable.ic_check_filled_outlined,
        iconSize = 22.dp,
        padding = Modifier.padding(start = 0.dp),
        backgroundColor = LightGreen,
        shadowColor = DarkGreen,
        roundedCornerShape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
    )
}