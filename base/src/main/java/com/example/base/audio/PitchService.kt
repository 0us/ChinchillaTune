package com.example.base.audio

import be.tarsos.dsp.pitch.DynamicWavelet
import be.tarsos.dsp.pitch.FastYin
import java.nio.ByteBuffer

class PitchService {

    val wavelet = DynamicWavelet(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)
    val fastYin = FastYin(SAMPLE_RATE.toFloat(), BUFFER_SIZE_RECORDING)

    fun getPitch(buffer: ByteArray): Double {
        return fastYin.getPitch(floatMe(buffer)).pitch.toDouble()
    }
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