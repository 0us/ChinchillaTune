package com.chinchillateam.ui

import androidx.compose.animation.ExperimentalAnimationApi
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
import timber.log.Timber

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

@ExperimentalAnimationApi
@Composable
fun EvenSpacedNotes(state: TunerState) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        val nearestNote = state.nearestNote()
        Timber.d("@@@@@@@@@@@@@@@@@")
        state.notes.notes.forEach {
            Note(it.name, it == nearestNote)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Note(name: String, isActive: Boolean) {
    val background = if (isActive) {
        MaterialTheme.colors.secondary
    } else {
        Color.Transparent
    }
    Box(
        Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body2
        )
    }
}
