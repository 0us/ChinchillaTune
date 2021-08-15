package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.base.audio.AudioCalculator
import com.example.base.audio.AudioService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(
    private val service: AudioService
) : ViewModel() {

    private val calc = AudioCalculator()

    private val samples = mutableListOf<Double>()
    private val sampleSize = 1

    fun record(): Flow<Double> = flow {
        for (item in service.record()) {
            calc.setBytes(item)
            emit(calc.frequency)
        }
    }
}
