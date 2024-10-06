package dev.benedek.rig

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun RigUi(
    imagePath: String,
    runtime: Long,
    presses: Int,
    finished: Boolean,
    doRender: Boolean,
    progressPercent: MutableState<Float>,
    alpha: MutableState<Boolean>,
    quality: MutableState<Int>,
    format: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                modifier = Modifier.padding(12.dp),
                fontSize = 16.sp
            )
            // TODO: make a card with text a reusable function.
            CustomCard(
                modifier = Modifier
            ) {
                Text(
                    text = "Clicks: $presses!",
                    modifier = Modifier,
                    fontSize = 16.sp
                )

            }



        }

        val checked = remember { mutableStateOf(false) }

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
            WelcomeScreenState(context, checked, alpha, quality, format)
        }

    }
}

@Composable
fun WelcomeScreenState(
    context: Context,
    checked: MutableState<Boolean>,
    alpha: MutableState<Boolean>,
    quality: MutableState<Int>,
    format: MutableState<String>
){
    Text(
        text = "Press start button, to begin rendering.",
        modifier = Modifier,
        fontSize = 16.sp
    )
    CustomCard {
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

            Switch(
                checked = checked.value,
                enabled = if (format.value == "PNG") true else false,
                onCheckedChange = {
                    checked.value = it
                    alpha.value = checked.value

                    val toast = Toast.makeText(context, alpha.value.toString(), Toast.LENGTH_SHORT)
                    toast.show()
                },
            )
        }

    }
    CustomCard {

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Image quality (compression)",
                    modifier = Modifier,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Text(text = "${quality.value}",
                    modifier = Modifier,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

            Slider(
                value = quality.value.toFloat(),
                onValueChange = { quality.value = it.roundToInt()},
                enabled = if (format.value == "JPEG") true else false,
                valueRange = 0f..100f,
                steps = 9,
            )
            val toast = Toast.makeText(context, quality.value.toString(), Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    CustomCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Format",
                modifier = Modifier,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Row {
                var pngToggled = if (format.value == "PNG") remember { mutableStateOf(true) } else remember { mutableStateOf(false) }
                var jpegToggled = if (format.value == "JPEG") remember { mutableStateOf(true) } else remember { mutableStateOf(false) }


                ToggleableButton("PNG", pngToggled, jpegToggled, Modifier.padding(3.dp))
                ToggleableButton("JPEG", jpegToggled, pngToggled, Modifier.padding(3.dp))

                format.value = if (pngToggled.value) "PNG" else if (jpegToggled.value) "JPEG" else ""

            }

        }

    }

}