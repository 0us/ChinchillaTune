package com.example.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue
import kotlin.math.log

@Composable
fun HzDisplay(state: TunerState) {
    val hertz by animateFloatAsState(targetValue = state.currentHz.toFloat())

    Column(
        Modifier.padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(fontSize = 4.em, text = "${hertz.toInt()} hz")
    }
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
