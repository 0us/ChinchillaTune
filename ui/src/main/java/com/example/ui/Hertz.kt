package com.example.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue
import kotlin.math.log

@Composable
fun RowScope.HzDisplay(hz: Double) {
    val hertz by animateFloatAsState(targetValue = hz.toFloat())
    BoxWithConstraints(contentAlignment = Alignment.Center) {

        Text(
            fontSize = 4.em,
            text = "${hertz.toInt()} hz",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentSize()
                .requiredSize(
                    width = with(LocalDensity.current) { constraints.maxWidth.toDp() },
                    height = 24.dp
                )
        )
    }
}

@Composable
fun Contain() {

}

@Composable
fun BigNote(state: TunerState) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val note = state.notes.notes.firstOrNull() {
            val pitchOffset = 12 * log(state.currentHz / it.hertz, 2.0)
            pitchOffset.absoluteValue < 0.5
        }?.name ?: ""

        Text(fontSize = 120.sp, text = note)
    }
}
