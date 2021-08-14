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
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


const val AUDIO_SOURCE =
    MediaRecorder.AudioSource.MIC // for raw audio, use MediaRecorder.AudioSource.UNPROCESSED, see note in MediaRecorder section

const val SAMPLE_RATE = 44100
const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_8BIT
val BUFFER_SIZE_RECORDING = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)

private const val LOG_TAG = "AudioRecordTest"

interface AudioService {
    fun start()
}

class DefaultAudioService @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioService {

    override fun start() {
        val audioRecord = startRecording()
        read(audioRecord)
    }

    fun stop() {
        throw CancellationException()
    }

    private fun read(audioRecord: AudioRecord) {
        val data =
            ByteArray(BUFFER_SIZE_RECORDING / 2) // assign size so that bytes are read in in chunks inferior to AudioRecord internal buffer size


        ByteArrayOutputStream().use {
            while (true) {
                val read: Int = audioRecord.read(data, 0, data.size)
                try {
                    it.write(data, 0, read)

                    println(it.toByteArray().contentToString())
                } catch (e: IOException) {
                    Log.d(TAG, "exception while writing to file")
                    e.printStackTrace()
                }
            }
        }

    }

    private fun startRecording(): AudioRecord {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
        audioRecord.startRecording()

        return audioRecord
    }
}
