package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.base.audio.tuning.NoteConfig
import com.example.base.audio.tuning.TuningConfig

@Composable
fun Tuner(tuningConfig: TuningConfig, hertz: Double = 300.0) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // arrow

        // todo get notes as argument
        Column() {
            TunerArrow(hertz = 300.0)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tuningConfig.notes.forEach {
                    Note(it, hertz)
                }
            }
        }
    }
}

@Composable
fun TunerArrow(hertz: Double) {
    val width = LocalConfiguration.current.screenWidthDp
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
            contentDescription = "Tuner note indicator",
            Modifier
                .absoluteOffset()
                .scale(2f)
        )
    }
}

@Composable
fun Note(note: NoteConfig, hertz: Double) {
    val color = if (hertz.coerceIn(note.hertz - 50..note.hertz + 50) == hertz) {
        Color.Green
    } else Color.LightGray
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clip(CircleShape)
            .background(color)
    ) {
        Text(
            text = note.name,
            fontSize = 12.em,
            fontWeight = FontWeight(600),
            color = Color.White,
        )
    }
}
