package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.base.audio.tuning.TuningConfig
import com.example.base.audio.tuning.tunings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
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
        val state = remember {
            TunerState()
        }

        LaunchedEffect(Unit) {
            launch(Dispatchers.IO) {
                audioVm.record().consumeAsFlow().collect { newHertz -> state.currentHz = newHertz }
            }
        }

        ChichillaBox(1f) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Weighted {
                    HzDisplay(state.currentHz)
                }
                Weighted {
                    TuningDropdown { state.notes = it }
                }
                Weighted {
                    Box {}
                }
            }
        }
        ChichillaBox(2f) {
            BigNote(state)
        }
        ChichillaBox(1f) {
            Column(modifier = Modifier.padding(horizontal = 45.dp)) {
                key(state.notes) {
                    Notes(state)
                }
                HzSlider(state, 45.dp)
            }
        }
    }
}

@Composable
inline fun RowScope.Weighted(content: @Composable () -> Unit) {
    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
        content()
    }
}

@Composable
inline fun ColumnScope.ChichillaBox(weight: Float, content: @Composable () -> Unit) {
    Box(
        Modifier
            .weight(weight)
            .fillMaxWidth(),
    ) {
        content()
    }
}


@Composable
inline fun TuningDropdown(
    crossinline onSelected: (TuningConfig) -> Unit = {}
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    var text: String by remember {
        mutableStateOf(tunings.first().name)
    }

    Box(Modifier.clickable { expanded = true }) {
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

class TunerState {

    var notes by mutableStateOf(tunings.first())

    //    var min = notes.notes.first().hertz.plusHalfSteps(-1)
    var min = notes.notes.first().hertz
        private set

    //    var max = notes.notes.last().hertz.plusHalfSteps(1)
    var max = notes.notes.last().hertz
        private set

    var currentHz by mutableStateOf(0.0)

    /**
     * Add x amount of halfSteps to [this]
     *
     * @param this a double representing a frequency on a logarithmic scale
     * @param halfSteps the amount of halfsteps to add
     */

    fun hzToPercent() = (ln(currentHz / min)) / (ln(max / min))
    fun hzToPercent(hz: Double) = (ln(hz / min)) / (ln(max / min))
    fun percentToHz(x: Double) = max.pow(x) * min.pow(-x + 1)
}

fun Double.plusHalfSteps(halfSteps: Int): Double {
    return this * 2.0.pow(halfSteps / 12.0)
}