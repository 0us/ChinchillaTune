package com.example.base.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


const val AUDIO_SOURCE =
    MediaRecorder.AudioSource.DEFAULT // for raw audio, use MediaRecorder.AudioSource.UNPROCESSED, see note in MediaRecorder section

const val SAMPLE_RATE = 44100
const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_FLOAT

/**
 * Factor by that the minimum buffer size is multiplied. The bigger the factor is the less
 * likely it is that samples will be dropped, but more memory will be used. The minimum buffer
 * size is determined by [AudioRecord.getMinBufferSize] and depends on the
 * recording settings.
 */
private const val BUFFER_SIZE_FACTOR = 2
val BUFFER_SIZE_RECORDING =
    AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT) * BUFFER_SIZE_FACTOR

interface AudioService {
    fun record(): ReceiveChannel<FloatArray>
}

@SuppressLint("MissingPermission")
class DefaultAudioService @Inject constructor() : AudioService, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job()

    private val recorder: AudioRecord =
        AudioRecord(
            AUDIO_SOURCE,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            BUFFER_SIZE_RECORDING
        )

    override fun record(): ReceiveChannel<FloatArray> {
        recorder.startRecording()
        return produce {
            val buffer = FloatArray(BUFFER_SIZE_RECORDING)
            while (true) {
                recorder.read(buffer, 0, BUFFER_SIZE_RECORDING, AudioRecord.READ_BLOCKING)
                send(buffer.copyOf())
            }
        }
    }
}
