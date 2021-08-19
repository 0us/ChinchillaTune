package com.example.chinchillatuner

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chinchillatuner.ui.theme.ChinchillaTunerTheme
import com.example.ui.Home
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
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
                    BackdropScaffold(
                        frontLayerShape = RoundedCornerShape(40.dp),
                        persistentAppBar = false,
                        stickyFrontLayer = true,
                        peekHeight = 60.dp,
                        scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed),
                        appBar = {
                            Box(
                                Modifier
                                    .height(60.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "ChinchillaTune",
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        backLayerBackgroundColor = MaterialTheme.colors.secondary,
                        backLayerContent = {
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_source_svg_file),
                                    contentDescription = ""
                                )
                            }
                        },
                        frontLayerBackgroundColor = MaterialTheme.colors.primary,
                        frontLayerContent = {

                            Home()

                        }
                    )
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
