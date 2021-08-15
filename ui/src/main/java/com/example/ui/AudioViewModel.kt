package com.example.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.base.audio.AudioCalculator
import com.example.base.audio.AudioService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(
    private val service: AudioService
) : ViewModel() {

    private val calc = AudioCalculator()

    private val samples = mutableListOf<Double>()
    private val sampleSize = 5

    var items = mutableStateListOf<Double>()
        private set

    fun record(): Flow<Double> = flow {
            service.record().collect {
                calc.setBytes(it)
                samples.add(calc.frequency)

                if (samples.size > sampleSize) {
                    emit(samples.average())
                    samples.clear()
                }
            }
    }
}
