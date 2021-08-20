package com.example.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.absoluteValue
import kotlin.math.log

private val ThumbDefaultElevation = 1.dp
private val ThumbPressedElevation = 6.dp
private val ThumbRippleRadius = 24.dp

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
                    val posFraction = state.hzToPercent(it.hertz).toFloat()
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

@Composable
inline fun HzFullSpectrumSlider(state: TunerState, padding: Dp) {
    BoxWithConstraints {
        val widthDp: Dp
        with(LocalDensity.current) {
            widthDp = constraints.maxWidth.toDp()
        }

        val acceptableRange =
            state.min..state.max.coerceAtLeast(0.0)

        Mover(state.currentHz, acceptableRange, widthDp)
    }
}

@Composable
inline fun HzNoteSlider(state: TunerState) {
    BoxWithConstraints {
        val widthDp: Dp
        with(LocalDensity.current) {
            widthDp = constraints.maxWidth.toDp()
        }

        val note = state.notes.notes.firstOrNull {
            val pitchOffset = 12 * log(state.currentHz / it.hertz, 2.0)
            pitchOffset.absoluteValue < 0.5
        }

        val range = if (note == null) {
            80.0..100.0
        } else {
            note.hertz.plusHalfSteps(-2)..note.hertz.plusHalfSteps(2)
        }

        Mover(state.currentHz, range, widthDp)
    }
}

@Composable
fun Mover(hz: Double, hzRange: ClosedFloatingPointRange<Double>, parentWidth: Dp) {
    Box(Modifier.fillMaxWidth()) {
        val size = 20.dp
        val enabled = true
        val elevation = if (!enabled) {
            ThumbPressedElevation
        } else {
            ThumbDefaultElevation
        }

        val offset = if (hz in hzRange) {
            parentWidth * hzToPercent(hz, hzRange.start, hzRange.endInclusive).toFloat()
        } else {
            if (hz > hzRange.endInclusive) {
                parentWidth
            } else {
                0.dp
            }
        }

        val animatedPos = animateDpAsState(targetValue = offset)

        Spacer(
            Modifier
                .offset(x = -(size / 2) + animatedPos.value)
                .size(size)
                .indication(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false, radius = ThumbRippleRadius)
                )
                .shadow(if (enabled) elevation else 0.dp, CircleShape, clip = false)
                .background(MaterialTheme.colors.secondaryVariant, CircleShape)
                .zIndex(999f)
        )
    }
}
