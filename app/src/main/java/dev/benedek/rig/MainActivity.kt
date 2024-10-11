@file:OptIn(ExperimentalMaterial3Api::class)

package dev.benedek.rig

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
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

                var presses by remember { mutableIntStateOf(0) }

                val progressPercent = remember { mutableFloatStateOf(0f) }
                val finished = remember { mutableStateOf(false) }
                val doRender = remember { mutableStateOf(false) }
                val alpha = remember { mutableStateOf(false) }
                val quality = remember { mutableIntStateOf(100) }
                val format = remember { mutableStateOf("PNG") }
                val width = remember { mutableIntStateOf(0) }
                val height = remember { mutableIntStateOf(0) }

                val outputPath = "${filesDir.absolutePath}/image.png"

                val context = LocalContext.current
                val thread = Thread {
                    val firstTime = System.currentTimeMillis()
                    finished.value = false
                    progressPercent.floatValue = 0f
                    val rig = RIG()


                    imagePath = rig.randomImageGenerator(
                            this,
                            progressPercent,
                            outputPath,
                            width.intValue,
                            height.intValue,
                            alpha.value,
                            quality.intValue,
                            format.value
                    )



                    val secondTime = System.currentTimeMillis()
                    runtime = secondTime - firstTime
                    progressPercent.floatValue = 0f
                    doRender.value = false
                    finished.value = true
                }

                if (doRender.value) {
                    thread.start()
                } else {
                    thread.interrupt()
                }
                val focusManager = LocalFocusManager.current
                Scaffold(
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
                                    IconButton(onClick = {
                                        finished.value = false
                                        doRender.value = false
                                    }) {
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
                                if (width.intValue > 1 || height.intValue > 1) {
                                    doRender.value = true
                                } else {
                                    val toast = Toast.makeText(context, "Resolution is not accepted", Toast.LENGTH_SHORT)
                                    toast.show()
                                }

                            }
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                        }
                    },

                ) { innerPadding ->
                    RigUi(
                        imagePath,
                        runtime,
                        presses,
                        finished.value,
                        doRender.value,
                        progressPercent,
                        alpha,
                        quality,
                        format,
                        width,
                        height,
                        modifier = Modifier
                            .padding(
                                top = innerPadding.calculateTopPadding(), // Only respect top padding
                                bottom = innerPadding.calculateBottomPadding() // Ignore bottom padding to render behind the BottomAppBar
                            )
                            .verticalScroll(state = rememberScrollState())
                            .pointerInput(Unit) {
                            detectTapGestures {
                                focusManager.clearFocus()
                            }
                        },
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
    modifier: Modifier = Modifier.fillMaxWidth(),
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
@Composable
fun formatButton(text: String, isToggled: MutableState<Boolean>, invalidate: MutableState<Boolean>, modifier: Modifier): Boolean { // Make invalidate parameter number flexible
    val haptic = LocalHapticFeedback.current

    if (isToggled.value) {
        Button(
            onClick = {
                if (isToggled.value) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }


            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = modifier,
        ) {
            Text(text)
        }
        return true
    } else {
        OutlinedButton(
            onClick = {
                isToggled.value = !isToggled.value // Toggle the color state
                invalidate.value = false
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = modifier,
        ) {
            Text(text)
        }
        return false
    }


}



@Preview(showBackground = true, showSystemUi = true, name = "Main screen")
@Composable
fun RigUIPreview() {
    val progressPercent = remember { mutableFloatStateOf(0f) }
    val alpha = remember { mutableStateOf(false) }
    val quality = remember { mutableIntStateOf(100) }
    val format = remember { mutableStateOf("PNG") }
    val width = remember { mutableIntStateOf(0) }
    val height = remember { mutableIntStateOf(0) }

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
                presses = presses,
                finished = false,
                doRender = false,
                progressPercent = progressPercent,
                alpha = alpha,
                quality = quality,
                format = format,
                width = width,
                height = height,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
