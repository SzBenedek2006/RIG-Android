@file:OptIn(ExperimentalMaterial3Api::class)

package dev.benedek.rig

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.benedek.rig.ui.theme.RIGTheme
import java.io.File

var imagePath = emptyArray<File>()
var runtime: Long = 0



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            RIGTheme {
                MainScreen()
            }
        }
    }
}






@Preview(showBackground = true, showSystemUi = true, name = "Main screen")
@Composable
fun RigUIPreview() {


    RIGTheme {
        val presses = 0
        Scaffold(
            topBar = {
                Surface(tonalElevation = 10.dp) {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        title = {
                            Text("RIG-Android")
                        },

                    )
                }
            },
        ) { innerPadding ->
            RigUi(
                imagePath = "Image path",
                runtime = 0,
                finished = false,
                doRender = false,
                stop = false,
                progressPercent = remember { mutableFloatStateOf(0f) },
                alpha = remember { mutableStateOf(false) },
                quality = remember { mutableIntStateOf(100) },
                format = remember { mutableStateOf("PNG") },
                width = remember { mutableIntStateOf(0) },
                height = remember { mutableIntStateOf(0) },
                count = remember { mutableIntStateOf(10) },
                currentCount = remember { mutableIntStateOf(1) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
