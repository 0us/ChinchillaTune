package com.example.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
            ChichillaBox(1f) {
                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HzDisplay(state)
                    TuningDropdown { state.notes = it }
                    Box {}
                }
            }
            ChichillaBox(2f) {
                BigNote(state)
                Text(text = "123123")
            }
            ChichillaBox(1f) {
                HzSlider(state = state)
                Notes(state = state)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0EA))
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun HzSlider(state: TunerState) {
    val sliderPos = animateFloatAsState(targetValue = state.hzToPercent().toFloat())
    Slider(sliderPos.value, onValueChange = {})
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

    Box(
        Modifier
            .padding(vertical = 16.dp)
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
    key(state.notes) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            with(LocalDensity.current) {
                state.notes.notes.forEach {
                    val pos = state.hzToPercent(it.hertz).toFloat()
                    Box(Modifier.offset(x = (constraints.maxWidth.toDp() * pos))) {
                        Text(text = it.name)
                    }
                }
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
    fun hzToPercent(hz: Double) = (ln(hz / min)) / (ln(max / min))
    fun percentToHz(x: Double) = max.pow(x) * min.pow(-x + 1)
}
