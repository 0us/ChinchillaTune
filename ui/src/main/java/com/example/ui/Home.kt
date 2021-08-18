package com.example.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.base.audio.tuning.TuningConfig
import com.example.base.audio.tuning.tunings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.ln
import kotlin.math.pow

@Composable
fun Home(audioVm: AudioViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val state = remember {
                TunerState()
            }

            LaunchedEffect(Unit) {
                launch(Dispatchers.IO) {
                    audioVm.record().collect { newHertz -> state.currentHz = newHertz }
                }
            }

            ChichillaRow(1f) {
                HzDisplay(state)
                TuningDropdown { state.notes = it }
                Box {}
            }
            ChichillaRow(2f) {
                BigNote(state)
            }
            ChichillaRow(1f) {
                HzSlider(state = state)
//                Notes(state = state)
            }
        }
    }
}

@Composable
fun HzSlider(state: TunerState) {
    val sliderPos = animateFloatAsState(targetValue = state.hzToPercent().toFloat())
    Slider(sliderPos.value, onValueChange = {})
}


@Composable
fun ColumnScope.ChichillaRow(weight: Float, content: @Composable () -> Unit) {
    Row(
        Modifier
            .weight(weight)
            .fillMaxWidth()
    ) {
        content()
    }
}


@Composable
fun TuningDropdown(
    onSelected: (TuningConfig) -> Unit = {}
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    var text: String by remember {
        mutableStateOf(tunings.first().name)
    }

    Box(
        Modifier
            .padding(12.dp)
            .clickable { expanded = true }) {
        Row {
            Text(text = text)
            Icon(imageVector = Icons.Default.ArrowDropDown, "")
        }

        Row {
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                tunings.forEach { tuning ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        text = tuning.name
                        onSelected(tuning)
                    }) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = tuning.name
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Notes(state: TunerState) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        Modifier
            .fillMaxWidth()
            .onSizeChanged {
                size = it
            }
    ) {
        state.notes.notes.forEach {
            val pos = state.hzToPercent()
            Box(Modifier.offset(x = (pos * size.width).dp)) {
                Text(text = it.name)
            }
        }
    }
}

class TunerState {

    var notes by mutableStateOf(tunings.first())

    var min = notes.notes.first().hertz.plusHalfSteps(-1)
        private set
    var max = notes.notes.last().hertz.plusHalfSteps(1)
        private set

    var currentHz by mutableStateOf(0.0)

    /**
     * Add x amount of halfSteps to [this]
     *
     * @param this a double representing a frequency on a logarithmic scale
     * @param halfSteps the amount of halfsteps to add
     */
    private fun Double.plusHalfSteps(halfSteps: Int): Double {
        return this * 2.0.pow(halfSteps / 12.0)
    }

    fun hzToPercent() = (ln(currentHz / min)) / (ln(max / min))
    fun percentToHz(x: Double) = max.pow(x) * min.pow(-x + 1)
}
