package com.example.chinchillatuner.viewmodels

import androidx.lifecycle.ViewModel
import com.example.chinchillatuner.audio.AudioService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(
    private val service: AudioService
) : ViewModel() {

    fun start() {
        service.start()
    }
}