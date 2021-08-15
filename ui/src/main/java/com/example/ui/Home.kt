package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.base.audio.tuning.TuningConfig
import com.example.base.audio.tuning.tunings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

            var selectedTuning by remember { mutableStateOf(tunings.first()) }
            var hertz by remember { mutableStateOf(0.0) }

            LaunchedEffect(true) {
                launch(Dispatchers.IO) { audioVm.record().collect { newHertz -> hertz = newHertz } }
            }

            TuningDropdown(tunings = tunings) {
                selectedTuning = it
            }

            Hertz(hertz)
            Tuner(selectedTuning, hertz)
        }
    }
}

@Composable
@Preview
fun TuningDropdown(
    tunings: Set<TuningConfig> = com.example.base.audio.tuning.tunings,
    onSelected: (TuningConfig) -> Unit = {}
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    var text: String by remember {
        mutableStateOf(tunings.first().name)
    }

    Box(
        Modifier
            .padding(24.dp)
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
                            modifier = Modifier.padding(16.dp),
                            text = tuning.name
                        )
                    }
                }
            }
        }
    }
}
