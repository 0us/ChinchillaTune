package com.chinchillateam.chinchillatune

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chinchillateam.chinchillatune.theme.ChinchillaTunerTheme
import com.chinchillateam.ui.Home
import com.example.chinchillatuner.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalAnimationApi
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
