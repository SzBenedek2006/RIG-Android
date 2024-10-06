@file:OptIn(ExperimentalMaterial3Api::class)

package dev.benedek.rig

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import dev.benedek.rig.ui.theme.RIGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var imagePath = ""
        var runtime: Long = 0

        enableEdgeToEdge()
        setContent {
            RIGTheme {
                // https://developer.android.com/develop/ui/compose/components/scaffold

                var presses by remember { mutableStateOf(0) }

                var progressPercent = remember { mutableStateOf(0f) }
                var finished by remember { mutableStateOf(false) }
                var doRender by remember { mutableStateOf(false) }
                var alpha = remember { mutableStateOf(false) }
                val outputPath = "${filesDir.absolutePath}/image.png"
                val width = 100
                val height = 100
                val quality = 100

                if (doRender) {
                    Thread(Runnable {
                        val firstTime = System.currentTimeMillis()
                        finished = false



                        val rig = RIG()
                        imagePath = rig.randomImageGenerator(this, progressPercent, outputPath, width, height, alpha, quality)


                        val secondTime = System.currentTimeMillis()
                        runtime = secondTime - firstTime
                        progressPercent.value = 0f
                        doRender = false
                        finished = true
                    }).start()

                }

                Scaffold( // Ide jön a topBar és a bottomBar és a FloatingActionButton
                    topBar = {
                        Surface(tonalElevation = 10.dp) {
                            TopAppBar(
                                colors = topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text("RIG-Android")
                                },
                                actions = {
                                    IconButton(onClick = {finished = false}) {
                                        Icon(
                                            imageVector = Icons.Sharp.Clear,
                                            contentDescription = "Clear images"
                                        )
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        Surface(tonalElevation = 10.dp) {
                            BottomAppBar(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = "Bottom app bar",
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                presses++
                                doRender = true
                            }
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                        }
                    },

                ) { innerPadding -> // Ide pedig az oldal tartalma, ez esetben egy függvény
                    RigUi(
                        imagePath,
                        runtime,
                        presses,
                        finished,
                        doRender,
                        progressPercent,
                        alpha,
                        modifier = Modifier
                            .padding(
                                top = innerPadding.calculateTopPadding(), // Only respect top padding
                                bottom = 0.dp // Ignore bottom padding to render behind the BottomAppBar
                            )
                            .verticalScroll(state = rememberScrollState())
                    )
                }
            }
        }
    }
}





fun loadBitmap(path: String): Bitmap? {
    return try {
        BitmapFactory.decodeFile(path)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    //onClick: (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Surface(
        modifier = modifier
            //.clickable(onClick = onClick ?: {})
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(2.dp, Color.Gray),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}





@Preview(showBackground = true, showSystemUi = true, name = "Main screen")
@Composable
fun RigUIPreview() {
    var progressPercent = remember { mutableStateOf(0f) }
    var alpha = remember { mutableStateOf(false) }
    RIGTheme {
        var presses = 0
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
                presses = presses,
                finished = false,
                doRender = false,
                progressPercent = progressPercent,
                alpha = alpha,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
