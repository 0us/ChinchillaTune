package com.chinchillateam.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chinchillateam.base.tuning.TuningConfig
import com.chinchillateam.base.tuning.tunings


@Composable
inline fun TuningDropdown(
    crossinline onSelected: (TuningConfig) -> Unit = {}
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    var text: String by remember {
        mutableStateOf(tunings.first().name)
    }

    // todo dropdown menu is lagging

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
