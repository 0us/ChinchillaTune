package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

@Preview
@Composable
fun Tuner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // arrow

        // todo get notes as argument
        Row {
            repeat(6) {
                Note("D")
            }
        }
    }
}

@Composable
fun TunerArrow(hertz: Double) {

}

@Composable
fun Note(note: String = "E") {
    Box(modifier = Modifier
        .clip(CircleShape)
        .background(Color.Cyan)
    ) {
        Text(text = note, fontSize = 16.em)
    }
}