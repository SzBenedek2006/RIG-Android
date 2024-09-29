@file:OptIn(ExperimentalMaterial3Api::class)

package dev.benedek.rig

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext // Import for LocalContext
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import dev.benedek.rig.ui.theme.RIGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firstTime = System.currentTimeMillis()
        val rig = RIG(this) // Using LocalContext here
        val imagePath = rig.randomImageGenerator()
        val secondTime = System.currentTimeMillis()
        val runtime = secondTime - firstTime
        enableEdgeToEdge()
        setContent {
            RIGTheme {
                // https://developer.android.com/develop/ui/compose/components/scaffold

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
                    }
                ) { innerPadding -> // Ide pedig az oldal tartalma, ez esetben egy függvény
                    RigUi(
                        path = imagePath,
                        time = runtime,
                        modifier = Modifier
                            .padding(
                                top = innerPadding.calculateTopPadding(), // Only respect top padding
                                bottom = 0.dp // Ignore bottom padding to render behind the BottomAppBar
                            )
                    )
                }
            }
        }
    }
}


@Composable
fun RigUi(path: String, time: Long, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = modifier.padding(top = 22.dp),
                text = "Say hello to RIG!",
                fontSize = 36.sp
            )
            Text(
                text = "Nice to see you here!",
                modifier = modifier,
                fontSize = 16.sp
            )
            Text(
                text = "Generated image succesfully in $time ms.\n path is :\n$path",
                modifier = modifier,
                fontSize = 16.sp
            )
            val bitmap = loadBitmap(path)
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp) // Adjust size as needed
                )
            } else {
                Text(text = "Failed to load image", fontSize = 16.sp)
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




@Preview(showBackground = true, showSystemUi = true, name = "Main screen")
@Composable
fun RigUIPreview() {
    RIGTheme {
        Scaffold(
            topBar = {
                Surface(tonalElevation = 10.dp) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
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
                path = "Image path",
                time = 0,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
