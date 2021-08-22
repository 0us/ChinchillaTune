package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.base.audio.tuning.NoteConfig
import com.example.base.audio.tuning.tunings
import com.example.base.audio.util.plusHalfSteps
import com.example.ui.viewmodel.AudioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.pow

@Composable
fun Debuggerr(state: TunerState) {
    Box(
        Modifier
            .background(Color.Black)
            .fillMaxWidth()
    ) {
        var value by remember { mutableStateOf(0f) }
        Slider(value, onValueChange = {
            state.currentHz = state.fractionToHz(it.toDouble())
            value = it
        })
    }
}

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

        ChichillaRow(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp), weight = 1f
        ) {
            Weighted {}
            Weighted { TuningDropdown { state.notes = it } }
            Weighted { Box {} }
        }

        ChichillaColumn(Modifier.padding(horizontal = 45.dp), weight = 2f) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Weighted {
                    HzDisplay(state.currentHz)
                }
                Weighted {
                    BigNote(state)
                }
                Weighted {}
            }
            HzNoteSlider(state = state)
            Politiradar()
            PolitiradarText(state = state)
        }
        ChichillaColumn(Modifier.padding(horizontal = 45.dp), 1f) {
            key(state.notes) {
                EvenSpacedNotes(state)
            }
        }
        Debuggerr(state = state)
    }
}


@Composable

inline fun RowScope.Weighted(content: @Composable () -> Unit) {
    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
        content()
    }
}

@Composable
inline fun ColumnScope.ChichillaRow(
    modifier: Modifier = Modifier,
    weight: Float,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        Modifier
            .weight(weight)
            .fillMaxWidth()
            .then(modifier),
    ) {
        this.content()
    }
}

@Composable
inline fun ColumnScope.ChichillaColumn(
    modifier: Modifier = Modifier,
    weight: Float,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        Modifier
            .weight(weight)
            .fillMaxWidth()
            .then(modifier),
    ) {
        this.content()
    }
}

class TunerState {

    // todo remember on app restart
    var notes by mutableStateOf(tunings.first())

    fun nearestNote(): NoteConfig {
        val fraction = hzToFraction(currentHz)
        return notes.notes.minByOrNull {
            abs(fraction - hzToFraction(it.hertz))
        }!!

    }

    var min = notes.notes.first().hertz.plusHalfSteps(-0.5)
        private set
    var max = notes.notes.last().hertz.plusHalfSteps(0.5)
        private set

    var currentHz by mutableStateOf(0.0)

    // todo calculate currentNote and fraction here

    fun hzToFraction() = (ln(currentHz / min)) / (ln(max / min))
    fun hzToFraction(hz: Double) = (ln(hz / min)) / (ln(max / min))
    fun fractionToHz(x: Double) = max.pow(x) * min.pow(-x + 1)
}
