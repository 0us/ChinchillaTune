package com.example.chinchillatuner

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chinchillatuner.ui.theme.ChinchillaTunerTheme
import com.example.ui.Home
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
                
                Surface(color = MaterialTheme.colors.primary) {

                    Home()
                }

                Surface(color = MaterialTheme.colors.primaryVariant, elevation = 5.dp) {
                    Box(modifier = Modifier.size(20.dp))
                }
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
