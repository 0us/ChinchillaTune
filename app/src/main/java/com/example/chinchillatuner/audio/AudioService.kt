package com.example.chinchillatuner.audio

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException


const val AUDIO_SOURCE =
    MediaRecorder.AudioSource.MIC // for raw audio, use MediaRecorder.AudioSource.UNPROCESSED, see note in MediaRecorder section

const val SAMPLE_RATE = 44100
const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_8BIT

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
    fun start(scope: CoroutineScope)
}

class DefaultAudioService @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioService, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job()

    override fun start(scope: CoroutineScope) {
        Recorder(scope).start()
    }

    inner class Recorder(private val scope: CoroutineScope) {
        private val recorder: AudioRecord

        init {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw IllegalArgumentException("No permission!")
            }

            val audioRecord = AudioRecord(
                AUDIO_SOURCE,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                BUFFER_SIZE_RECORDING
            )

            if (audioRecord.state != AudioRecord.STATE_INITIALIZED) { // check for proper initialization
                throw IllegalArgumentException("NOOOO!")
            }
            recorder = audioRecord
        }

        fun stop() {
            throw CancellationException()
        }

        fun start() {

            val buffer =
                ByteBuffer.allocateDirect(BUFFER_SIZE_RECORDING) // assign size so that bytes are read in in chunks inferior to AudioRecord internal buffer size

            recorder.startRecording()

            val job = scope.launch {
                ByteArrayOutputStream().use { out ->
                    while (true) {
                        recorder.read(buffer, BUFFER_SIZE_RECORDING)
                        try {
                            out.write(buffer.array(), 0, BUFFER_SIZE_RECORDING)
                            buffer.clear()

                            println(out.toByteArray().average())
                        } catch (e: IOException) {
                            Log.d(TAG, "exception while writing to buffer")
                            e.printStackTrace()
                        }
                    }
                }
            }

            job.invokeOnCompletion {
                recorder.stop()
                recorder.release()
            }
        }
    }
}
