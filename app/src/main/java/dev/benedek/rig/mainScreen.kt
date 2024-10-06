package dev.benedek.rig

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RigUi(imagePath: String, runtime: Long, presses: Int, finished: Boolean, doRender: Boolean, progressPercent: MutableState<Float>, alpha: MutableState<Boolean>, modifier: Modifier = Modifier) {
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

        var checked by remember { mutableStateOf(false) }

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

                    Switch(
                        checked = checked,
                        enabled = true,
                        onCheckedChange = {
                            checked = it
                            alpha.value = true
                        },
                    )
                }

            }
        }

    }
}