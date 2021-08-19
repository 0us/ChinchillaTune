package com.example.ui

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

private val ThumbDefaultElevation = 1.dp
private val ThumbPressedElevation = 6.dp
private val ThumbRippleRadius = 24.dp

@Composable
fun Notes(state: TunerState) {
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
                        Text(text = it.name, textAlign = TextAlign.Center, style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}

@Composable
inline fun HzSlider(state: TunerState, padding: Dp) {
    BoxWithConstraints {
        val widthDp: Dp
        with(LocalDensity.current) {
            widthDp = constraints.maxWidth.toDp()
        }

        val acceptableRange =
            state.min..state.max.coerceAtLeast(0.0)

        if (state.currentHz in acceptableRange) {
            val position = state.hzToPercent().toFloat()
            val animatedPos = animateFloatAsState(targetValue = position)
            val offset = widthDp * animatedPos.value
            Mover(offset)
        }


    }
}

@Composable
fun Mover(offset: Dp) {
    Box(Modifier.fillMaxWidth()) {
        val size = 20.dp
        val enabled = true
        val elevation = if (!enabled) {
            ThumbPressedElevation
        } else {
            ThumbDefaultElevation
        }

        Spacer(
            Modifier
                .offset(x = -(size / 2) + offset)
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
