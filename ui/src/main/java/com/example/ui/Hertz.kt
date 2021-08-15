package com.example.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode


@Composable
fun HertzContainer(viewModel: AudioViewModel = viewModel()) {
    Hertz(0.0)
}

@Preview
@Composable
fun Hertz(state: Double = 1243.0) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxWidth()
    )
    {
        val hertz by animateFloatAsState(targetValue = state.toFloat())

        Text(
            fontSize = 24.em,
            text = BigDecimal(hertz.toDouble()).setScale(1, RoundingMode.HALF_EVEN).toString()
        )
    }
}
