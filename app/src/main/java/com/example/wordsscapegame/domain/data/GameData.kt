package com.example.wordsscapegame.data

import androidx.compose.ui.graphics.Color
import com.example.wordsscapegame.ui.screens.Animation
import com.example.wordsscapegame.ui.screens.Position
import com.example.wordsscapegame.ui.screens.Word
import com.example.wordsscapegame.ui.theme.Green
import com.example.wordsscapegame.ui.theme.Orange
import com.example.wordsscapegame.ui.theme.Peach
import com.example.wordsscapegame.ui.theme.Purple
import com.example.wordsscapegame.ui.theme.Red
import javax.inject.Inject

class GameData @Inject constructor() {
    fun getMovingWords(): List<Word> {
        return listOf(
            Word(
                text = "Apple",
                position = Position.Start,
                color = Red,
                caught = false,
                animation = Animation(
                    duration = 4000,
                    delay = 0
                )
            ),
            Word(
                text = "Banana",
                position = Position.Start,
                color = Color.Yellow,
                caught = false,
                animation = Animation(
                    duration = 3500,
                    delay = 500
                )
            ),
            Word(
                text = "Grape",
                position = Position.Start,
                color = Purple,
                caught = false,
                animation = Animation(
                    duration = 3000,
                    delay = 0
                )
            ),
            Word(
                text = "Orange",
                position = Position.Start,
                color = Orange,
                caught = false,
                animation = Animation(
                    duration = 2500,
                    delay = 1500
                )
            ),
            Word(
                text = "Watermelon",
                position = Position.Start,
                color = Green,
                caught = false,
                animation = Animation(
                    duration = 2000,
                    delay = 1500
                )
            ),
            Word(
                text = "Lemon",
                position = Position.Start,
                color = Color.Yellow,
                caught = false,
                animation = Animation(
                    duration = 1500,
                    delay = 500
                )
            ),
            Word(
                text = "Peach",
                position = Position.Start,
                color = Peach,
                caught = false,
                animation = Animation(
                    duration = 1500,
                    delay = 2500
                )
            )
        )
    }
}