package com.example.wordsscapegame.data

import androidx.compose.ui.graphics.Color
import com.example.wordsscapegame.ui.screens.Animation
import com.example.wordsscapegame.ui.screens.Position
import com.example.wordsscapegame.ui.screens.Word
import javax.inject.Inject

class GameRepository @Inject constructor() {
    fun getMovingWords(): List<Word> {
        return listOf(
            Word(
                name = "Apple",
                position = Position.Start,
                color = Color.Red,
                caught = false,
                animation = Animation(
                    duration = 4000,
                    delay = 0
                )
            ),
            Word(
                name = "Banana",
                position = Position.Start,
                color = Color.Yellow,
                caught = false,
                animation = Animation(
                    duration = 3500,
                    delay = 500
                )
            ),
            Word(
                name = "Grape",
                position = Position.Start,
                color = Color.Magenta,
                caught = false,
                animation = Animation(
                    duration = 3000,
                    delay = 0
                )
            ),
            Word(
                name = "Orange",
                position = Position.Start,
                color = Color(255, 165, 0),
                caught = false,
                animation = Animation(
                    duration = 2500,
                    delay = 1500
                )
            ),
            Word(
                name = "Kiwi",
                position = Position.Start,
                color = Color.Green,
                caught = false,
                animation = Animation(
                    duration = 2000,
                    delay = 1500
                )
            ),
            Word(
                name = "Lemon",
                position = Position.Start,
                color = Color.Yellow,
                caught = false,
                animation = Animation(
                    duration = 1500,
                    delay = 500
                )
            ),
            Word(
                name = "Peach",
                position = Position.Start,
                color = Color(255, 218, 185),
                caught = false,
                animation = Animation(
                    duration = 2300,
                    delay = 2500
                )
            )
        )
    }
}