package com.example.base.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


const val AUDIO_SOURCE =
    MediaRecorder.AudioSource.DEFAULT // for raw audio, use MediaRecorder.AudioSource.UNPROCESSED, see note in MediaRecorder section

const val SAMPLE_RATE = 44100
const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_16BIT

/**
 * Factor by that the minimum buffer size is multiplied. The bigger the factor is the less
 * likely it is that samples will be dropped, but more memory will be used. The minimum buffer
 * size is determined by [AudioRecord.getMinBufferSize] and depends on the
 * recording settings.
 */
private const val BUFFER_SIZE_FACTOR = 2
val BUFFER_SIZE_RECORDING =
    AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT) * BUFFER_SIZE_FACTOR

private const val LOG_TAG = "AudioRecordTest"

interface AudioService {
    fun record(): ReceiveChannel<ByteArray>
}

class DefaultAudioService @Inject constructor() : AudioService, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job()

    @SuppressLint("MissingPermission")
    private val recorder: AudioRecord = AudioRecord(
        AUDIO_SOURCE,
        SAMPLE_RATE,
        CHANNEL_CONFIG,
        AUDIO_FORMAT,
        BUFFER_SIZE_RECORDING
    )

    override fun record(): ReceiveChannel<ByteArray> {
        recorder.startRecording()

        return produce {
            val buffer = ByteBuffer.allocateDirect(BUFFER_SIZE_RECORDING)
            while (true) {
                recorder.read(buffer, BUFFER_SIZE_RECORDING)
                send(buffer.array().clone())
                buffer.clear()
            }
        }
    }
}
