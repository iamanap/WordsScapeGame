package com.example.wordsscapegame.data

import androidx.compose.ui.graphics.Color
import com.example.wordsscapegame.ui.Position
import com.example.wordsscapegame.ui.Word
import javax.inject.Inject

class GameRepository @Inject constructor() {
    fun getMovingWords(): List<Word> {
        return listOf(
            Word(
                name = "Apple",
                position = Position.Start,
                color = Color.Red,
                caught = false
            ),
            Word(
                name = "Banana",
                position = Position.Start,
                color = Color.Yellow,
                caught = false
            ),
            Word(
                name = "Grape",
                position = Position.Start,
                color = Color.Magenta,
                caught = false
            ),
            Word(
                name = "Orange",
                position = Position.Start,
                color = Color(255, 165, 0),
                caught = false
            ),
            Word(
                name = "Kiwi",
                position = Position.Start,
                color = Color.Green,
                caught = false
            ),
            Word(
                name = "Lemon",
                position = Position.Start,
                color = Color.Yellow,
                caught = false
            ),
//    Word(
//        name = "Blueberry",
//        position = Position.Start,
//        color = Color.Blue
//    ),
//    Word(
//        name = "Strawberry",
//        position = Position.Start,
//        color = Color.Red
//    ),
//    Word(
//        name = "Watermelon",
//        position = Position.Start,
//        color = Color(34, 139, 34)
//    ),
            Word(
                name = "Peach",
                position = Position.Start,
                color = Color(255, 218, 185),
                caught = false
            )
        )
    }
}