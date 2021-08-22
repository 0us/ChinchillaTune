package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import kotlin.math.log


@Composable
fun PolitiradarText(state: TunerState) {
    // todo single source of truth for currentNote
    val note = state.notes.notes.firstOrNull() {
        val pitchOffset = 12 * log(state.currentHz / it.hertz, 2.0)
        pitchOffset.absoluteValue < 0.5
    }
    note?.let {
        Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "")
            Text(text = note.hertz.toString())
            Text(text = "")
        }
    }
}

@Composable
fun Politiradar() {
    val numberOfLines = 29
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // todo can draw this in a canvas?
        repeat(numberOfLines) {
            val height = if (it == 0 || it == 15 || it == 28) 88.dp else 80.dp
            println(it)
            Spacer(
                modifier = Modifier
                    .size(2.dp, height)
                    .background(color = MaterialTheme.colors.secondaryVariant)
            )
        }
    }
}