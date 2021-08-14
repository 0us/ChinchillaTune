package com.example.chinchillatuner

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chinchillatuner.ui.theme.ChinchillaTunerTheme
import com.example.chinchillatuner.viewmodels.AudioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val audioViewModel: AudioViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fakeTunings = setOf(
            "Standard",
            "New standard",
            "Drop D"
        )


        val x = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(this, "ja", Toast.LENGTH_LONG).show()
        }

        x.launch(android.Manifest.permission.RECORD_AUDIO)

        audioViewModel.start()

        setContent {
            ChinchillaTunerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        TuningDropdown(tunings = fakeTunings) {}
                    }
                }
            }
        }
    }

    private var permissionToRecordAccepted = false
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }
}

@Composable
fun TuningDropdown(tunings: Set<String>, onSelected: () -> Unit) {
    var expanded: Boolean = remember { false }

    DropdownMenu(expanded = expanded, onDismissRequest = { /*TODO*/ }) {
        tunings.forEach { tuning ->
            DropdownMenuItem(onClick = { expanded = false }) {
                Text(text = tuning)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChinchillaTunerTheme {
        Greeting("Android")
    }
}