package com.example.chinchillatuner

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chinchillatuner.ui.theme.ChinchillaTunerTheme
import com.example.ui.HertzContainer
import com.example.ui.Tuner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val x = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        x.launch(android.Manifest.permission.RECORD_AUDIO)

        Toast.makeText(this, "start", Toast.LENGTH_LONG).show()

        setContent {
            ChinchillaTunerTheme {
                // A surface container using the 'background' color from the theme

                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                    ) {
                        DontBlockPlz {}
                        Column(verticalArrangement = Arrangement.Bottom) {
                            HertzContainer()
                            Tuner()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DontBlockPlz(onClick: () -> Unit) {
    var i by remember { mutableStateOf(0) }
    Button(onClick = { i++ }) {
        Text(i.toString())
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

@Composable
fun DefaultPreview() {
    ChinchillaTunerTheme {
        Greeting("Android")
    }
}
