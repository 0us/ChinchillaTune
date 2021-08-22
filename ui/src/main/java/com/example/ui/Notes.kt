package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import kotlin.math.log

@Composable
fun SpectrumSpacedNotes(state: TunerState) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val widthDp: Dp
        with(LocalDensity.current) {
            widthDp = constraints.maxWidth.toDp()
        }

        Column {

            val size = 20.dp
            val sizeOffset = size / 2
            Box {
                state.notes.notes.forEach {
                    val posFraction = state.hzToFraction(it.hertz).toFloat()
                    Box(
                        Modifier
                            .requiredSize(size)
                            .offset(x = (widthDp * posFraction) - sizeOffset),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it.name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun EvenSpacedNotes(state: TunerState) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        state.notes.notes.forEach {
            val pitchOffset = 12 * log(state.currentHz / it.hertz, 2.0)
            val isCurrent = pitchOffset.absoluteValue < 0.5
            Box(
                Modifier
                    .clip(CircleShape)
                    .then(
                        if (isCurrent) {
                            Modifier.background(Color.Green)
                        } else {
                            Modifier.background(Color.Transparent)
                        }
                    )
            ) {
                Text(
                    text = it.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}
