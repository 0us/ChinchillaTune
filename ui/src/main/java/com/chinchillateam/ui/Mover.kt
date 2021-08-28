package com.chinchillateam.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.chinchillateam.base.util.hzToPercent
import com.chinchillateam.base.util.plusHalfSteps

private val ThumbDefaultElevation = 1.dp
private val ThumbPressedElevation = 6.dp
private val ThumbRippleRadius = 24.dp

@Composable
fun HzFullSpectrumSlider(state: TunerState, padding: Dp) {
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
fun HzNoteSlider(state: TunerState) {
    BoxWithConstraints {
        val widthDp: Dp
        with(LocalDensity.current) {
            widthDp = constraints.maxWidth.toDp()
        }

//        val note = state.notes.notes.firstOrNull {
//            val pitchOffset = 12 * log(state.currentHz / it.hertz, 2.0)
//            pitchOffset.absoluteValue < 0.5
//        }

        val note = state.nearestNote()
        val range = note.hertz.plusHalfSteps(-0.5)..note.hertz.plusHalfSteps(0.5)

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
                .offset(x = animatedPos.value)
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
