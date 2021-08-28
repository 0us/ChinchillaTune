package com.chinchillateam.base.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import javax.inject.Inject


const val AUDIO_SOURCE =
    MediaRecorder.AudioSource.MIC // for raw audio, use MediaRecorder.AudioSource.UNPROCESSED, see note in MediaRecorder section

const val SAMPLE_RATE = 22050
const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_FLOAT

/**
 * Factor by that the minimum buffer size is multiplied. The bigger the factor is the less
 * likely it is that samples will be dropped, but more memory will be used. The minimum buffer
 * size is determined by [AudioRecord.getMinBufferSize] and depends on the
 * recording settings.
 */
private const val BUFFER_SIZE_FACTOR = 1
val BUFFER_SIZE_RECORDING =
    AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT) * BUFFER_SIZE_FACTOR

interface AudioService {
    fun record(scope: CoroutineScope): ReceiveChannel<FloatArray>
}

@SuppressLint("MissingPermission")
@ExperimentalCoroutinesApi
class DefaultAudioService @Inject constructor() : AudioService {

    private lateinit var recorder: AudioRecord
    private lateinit var buffer: FloatArray
    private lateinit var job: Job

    override fun record(scope: CoroutineScope): ReceiveChannel<FloatArray> {
        job = Job()

        return scope.produce(job + Dispatchers.IO) {

            recorder = AudioRecord(
                AUDIO_SOURCE,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                BUFFER_SIZE_RECORDING
            )

            recorder.startRecording()
            buffer = FloatArray(BUFFER_SIZE_RECORDING)

            launch {
                while (true) {

                    val read = recorder.read(
                        buffer,
                        0,
                        BUFFER_SIZE_RECORDING,
                        AudioRecord.READ_BLOCKING
                    )

                    if (read > 0) {
                        send(buffer.copyOf())
                    }
                }
            }
        }
    }
}
