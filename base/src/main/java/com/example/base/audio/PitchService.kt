package com.example.base.audio

import be.tarsos.dsp.pitch.DynamicWavelet
import be.tarsos.dsp.pitch.FastYin
import be.tarsos.dsp.pitch.PitchDetector
import java.nio.ByteBuffer
import kotlin.system.measureTimeMillis

class PitchService {

    //    val ggeortz = be.tarsos.dsp.pitch.GeneralizedGoertzel(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
//    val geortz = be.tarsos.dsp.pitch.Goertzel(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    val mcleod = be.tarsos.dsp.pitch.McLeodPitchMethod(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    val wavelet = DynamicWavelet(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    val fanYin = FastYin(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    val amdf = be.tarsos.dsp.pitch.AMDF(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)

    fun getPitch(buffer: FloatArray): Double {
        return fanYin.pitch(buffer)
    }

    fun getAllPitches(buffer: FloatArray): List<Double> {
        val list: List<Double>
        measureTimeMillis {
            list = listOf(
                mcleod.pitch(buffer),
                wavelet.pitch(buffer),
                fanYin.pitch(buffer),
                amdf.pitch(buffer)
            )
        }.also { println(it) }
        return list
    }

    inline fun PitchDetector.pitch(buffer: FloatArray): Double =
        this.getPitch(buffer).pitch.toDouble()
}

fun floatMe(pcms: ByteArray): FloatArray {
    val floaters = FloatArray(pcms.size)
    for (i in pcms.indices) {
        floaters[i] = pcms[i].toFloat()
    }
    return floaters
}

fun shortMe(bytes: ByteArray): ShortArray? {
    val out = ShortArray(bytes.size / 2) // will drop last byte if odd number
    val bb: ByteBuffer = ByteBuffer.wrap(bytes)
    for (i in out.indices) {
        out[i] = bb.getShort()
    }
    return out
}