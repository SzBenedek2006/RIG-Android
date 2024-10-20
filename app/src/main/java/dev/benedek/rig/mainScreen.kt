package dev.benedek.rig

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.roundToInt
import androidx.compose.ui.layout.ContentScale

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
    width: MutableIntState,
    height: MutableIntState,
    count: MutableIntState,
    currentCount: MutableIntState,
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
                        text = "Generated image successfully in $runtime ms.\n path is :\n$imagePath",
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



            Dialog(onDismissRequest = {}) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Rendering...",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 8.dp) // Add a little space below the title if needed
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LinearProgressIndicator(
                                progress = { progressPercent.value },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = "${"%.2f".format(progressPercent.value * 100)}%",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LinearProgressIndicator(
                                progress = { currentCount.intValue / count.intValue.toFloat() },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = "${currentCount.intValue} / ${count.intValue}",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }





        } else {
            WelcomeScreenState(context, checked, alpha, quality, format, width, height)
        }

    }
}

@Composable
fun WelcomeScreenState(
    context: Context,
    checked: MutableState<Boolean>,
    alpha: MutableState<Boolean>,
    quality: MutableState<Int>,
    format: MutableState<String>,
    width: MutableState<Int>,
    height: MutableState<Int>,
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
            val haptic = LocalHapticFeedback.current
            Switch(
                checked = checked.value,
                enabled = format.value == "PNG",
                onCheckedChange = {
                    checked.value = it
                    alpha.value = checked.value
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
            val haptic = LocalHapticFeedback.current
            Slider(
                value = quality.value.toFloat(),
                onValueChange = {
                    val newValue = it.roundToInt()


                    if (quality.value % 10 == 0 && newValue != quality.value) {
                        quality.value = newValue

                        if (quality.value != 100 && quality.value != 0) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        } else {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }


                                },
                enabled = format.value == "JPEG",
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
                val pngToggled = if (format.value == "PNG") remember { mutableStateOf(true) } else remember { mutableStateOf(false) }
                val jpegToggled = if (format.value == "JPEG") remember { mutableStateOf(true) } else remember { mutableStateOf(false) }


                formatButton("PNG", pngToggled, jpegToggled, Modifier.padding(3.dp))
                formatButton("JPEG", jpegToggled, pngToggled, Modifier.padding(3.dp))

                format.value = if (pngToggled.value) "PNG" else if (jpegToggled.value) "JPEG" else ""

            }

        }

    }

    CustomCard {


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Resolution",
                modifier = Modifier,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {

                val widthText = if (width.value == 0) remember { mutableStateOf("") } else remember { mutableStateOf(width.value.toString()) }
                OutlinedTextField(
                    value = widthText.value,
                    onValueChange = {newWidth ->
                        widthText.value = newWidth // For correct preview
                        width.value = newWidth.toIntOrNull() ?: 0
                    },
                    label = { Text("width") },
                    //placeholder = { Text(text = "enter width", modifier = Modifier.alpha(0.5F)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .requiredWidth(100.dp)
                        .padding(end = 8.dp)
                )
                val heightText = if (height.value == 0) remember { mutableStateOf("") } else remember { mutableStateOf(height.value.toString()) }
                OutlinedTextField(
                    value = heightText.value,
                    onValueChange = {newHeight ->
                        heightText.value = newHeight // For correct preview
                        height.value = newHeight.toIntOrNull() ?: 0
                    },
                    label = { Text("height") },
                    //placeholder = { Text(text = "enter height", modifier = Modifier.alpha(0.5F)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .requiredWidth(100.dp)
                        .padding(start = 8.dp)
                )
            }
        }
    }


}