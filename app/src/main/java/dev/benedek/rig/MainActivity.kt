@file:OptIn(ExperimentalMaterial3Api::class)

package dev.benedek.rig

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

                if (doRender) {
                    Thread(Runnable {
                        finished = false
                        val firstTime = System.currentTimeMillis()
                        val rig = RIG(this) // Using LocalContext here
                        imagePath = rig.randomImageGenerator(progressPercent)
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



@Composable
fun RigUi(imagePath: String, runtime: Long, presses: Int, finished: Boolean, doRender: Boolean, progressPercent: MutableState<Float>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween, // Distribute space between top and bottom
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier,//.fillMaxSize(),
            //verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .padding(30.dp)
                    .padding(top = 22.dp),
                text = "Say hello to RIG!",
                fontSize = 36.sp
            )
            Text(
                text = "Nice to see you here!",
                modifier = Modifier.padding(22.dp),
                fontSize = 16.sp
            )
            // TODO: make a card with text a reusable function.
            CustomCard {
                Text(
                    text = "Clicks: $presses!",
                    modifier = Modifier,
                    fontSize = 16.sp
                )

            }



        }
        if (finished) {
            Column(
                modifier = Modifier,//.fillMaxSize(),
                //verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bitmap = loadBitmap(imagePath)
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp) // Adjust size as needed
                    )
                    Text(
                        text = "Generated image succesfully in $runtime ms.\n path is :\n$imagePath",
                        modifier = Modifier,
                        fontSize = 16.sp
                    )
                } else {
                    Text(text = "Failed to load image", fontSize = 16.sp)
                }
            }
        } else if (doRender){
            Text(
                text = "Rendering...",
                modifier = Modifier,
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                LinearProgressIndicator(
                    progress = {(progressPercent.value)},
                    modifier = Modifier.padding(10.dp)

                )
                Text(
                    text = "${"%.2f".format(progressPercent.value * 100)}%",
                    modifier = Modifier.padding(10.dp)
                )
            }

        } else {
            Text(
                text = "Press start button, to begin rendering.",
                modifier = Modifier,
                fontSize = 16.sp
            )
            CustomCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Alpha (transparent pixels)",
                        modifier = Modifier,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    var checked by remember { mutableStateOf(false) }
                    Switch(
                        checked = checked,
                        enabled = true,
                        onCheckedChange = { checked = it},
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
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
