package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.base.audio.AudioCalculator
import com.example.base.audio.AudioService
import com.example.base.audio.PitchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


@HiltViewModel
class AudioViewModel @Inject constructor(
    private val service: AudioService
) : ViewModel() {

    val pitch = PitchService()

    suspend fun record(): Flow<Double> = flow {
        service.record().consumeAsFlow().collect {
            val pitch = pitch.getPitch(it)
            if (pitch != -1.0) {
                emit(pitch)
            }
        }
    }
}
