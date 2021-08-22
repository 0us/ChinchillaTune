package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.audio.audio.AudioService
import com.example.base.audio.audio.PitchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import javax.inject.Inject


@HiltViewModel
class AudioViewModel @Inject constructor(
    private val service: AudioService
) : ViewModel() {

    private val pitch = PitchService()

    suspend fun record(): ReceiveChannel<Double> = viewModelScope.produce(Dispatchers.IO) {
        for (floats in service.record(this)) {
            val pitch = pitch.getPitch(floats)
            if (pitch != -1.0) {
                send(pitch)
            }
        }
    }
}
