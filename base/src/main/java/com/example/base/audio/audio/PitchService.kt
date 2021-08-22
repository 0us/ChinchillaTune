package com.example.base.audio.audio

import be.tarsos.dsp.pitch.DynamicWavelet
import be.tarsos.dsp.pitch.FastYin
import be.tarsos.dsp.pitch.PitchDetector

class PitchService {

    //    private val ggeortz = be.tarsos.dsp.pitch.GeneralizedGoertzel(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
//    private val geortz = be.tarsos.dsp.pitch.Goertzel(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    private val mcleod = be.tarsos.dsp.pitch.McLeodPitchMethod(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    private val wavelet = DynamicWavelet(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    private val fanYin = FastYin(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    private val amdf = be.tarsos.dsp.pitch.AMDF(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    // todo test different algos

    fun getPitch(buffer: FloatArray): Double {
        return fanYin.pitch(buffer)
    }

    private inline fun PitchDetector.pitch(buffer: FloatArray): Double =
        this.getPitch(buffer).pitch.toDouble()
}
