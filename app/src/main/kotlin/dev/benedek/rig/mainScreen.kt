package dev.benedek.rig

import android.app.Notification
import android.content.Context
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt
import android.app.NotificationManager as NotificationManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    // https://developer.android.com/develop/ui/compose/components/scaffold





    var presses by remember { mutableIntStateOf(0) }

    val progressPercent = remember { mutableStateOf<Float?>(null) }

    val finished = remember { mutableStateOf(false) }
    val doRender = remember { mutableStateOf(false) }
    val alpha = remember { mutableStateOf(false) }
    val quality = remember { mutableIntStateOf(100) }
    val format = remember { mutableStateOf("PNG") }
    val width = remember { mutableIntStateOf(0) }
    val height = remember { mutableIntStateOf(0) }
    val count = remember { mutableIntStateOf(1) } // 10 for now, set to 0
    val currentCount = remember { mutableIntStateOf(1) }
    val stop = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val outputPath = context.filesDir.absolutePath


    val rigThread = Thread {
        stop.value = false
        val firstTime = System.currentTimeMillis()
        finished.value = false
        progressPercent.value = 0f
        val rig = RIG()


        rig.randomImageGenerator(
            context,
            progressPercent,
            outputPath,
            width.intValue,
            height.intValue,
            alpha.value,
            quality.intValue,
            format.value,
            count.intValue,
            currentCount,
            stop
        )



        val secondTime = System.currentTimeMillis()
        runtime = secondTime - firstTime
        progressPercent.value = 0f
        doRender.value = false
        finished.value = true
    }

    if (doRender.value) {
        rigThread.start()
    } else {
        rigThread.interrupt()
    }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }



    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                focusManager.clearFocus()
            }
        }
    }


    TopAppBarDefaults.topAppBarColors()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

//    containerColor: Color = Color.Unspecified,
//    scrolledContainerColor: Color = Color.Unspecified,
//    navigationIconContentColor: Color = Color.Unspecified,
//    titleContentColor: Color = Color.Unspecified,
//    actionIconContentColor: Color = Color.Unspecified


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(tonalElevation = 0.dp) {
                LargeTopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    title = {
                        Text(
                            text = "RIG-Android",
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .alpha(scrollBehavior.state.collapsedFraction.pow(8))
                        )
                        Text(
                            text = "Say hello to RIG!",
                            textAlign = TextAlign.Center,
                            fontSize = 36.sp,
                            modifier = Modifier
                                .alpha((1 - scrollBehavior.state.collapsedFraction).pow(8))
                                .fillMaxWidth()
                        )

                    },
                    actions = {
                        IconButton(onClick = { stop.value = true }) {
                            Icon(
                                imageVector = Icons.Sharp.Clear,
                                contentDescription = "Clear images"
                            )
                        }
                        IconButton(onClick = { navController.navigate("settings") }) { // Navigate to settings
                            Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
//                    bottomBar = {
//                        Surface(tonalElevation = 10.dp) {
//                            BottomAppBar(
//                                containerColor = MaterialTheme.colorScheme.primaryContainer,
//                                contentColor = MaterialTheme.colorScheme.primary
//                            ) {
//                                Text(
//                                    modifier = Modifier
//                                        .fillMaxWidth(),
//                                    textAlign = TextAlign.Center,
//                                    text = "Bottom app bar",
//                                )
//                            }
//                        }
//                    },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    presses++
                    if (width.intValue > 0 && height.intValue > 0 && count.intValue > 0) {
                        doRender.value = true
                    } else {
                        val toast = Toast.makeText(context, "Resolution or count is too small. Set to at least 1!", Toast.LENGTH_SHORT)
                        toast.show()
                    }

                },
                interactionSource = interactionSource
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start")
            }
        },

        ) { innerPadding ->
        RigUi(
            outputPath,
            runtime,
            finished.value,
            doRender.value,
            stop.value,
            progressPercent,
            alpha,
            quality,
            format,
            width,
            height,
            count,
            currentCount,
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(), // Don't render behind the top bar
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




@Composable
fun RigUi(
    imagePath: String,
    runtime: Long,
    finished: Boolean,
    doRender: Boolean,
    stop: Boolean,
    progressPercent: MutableState<Float?>,
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
                text = "Nice to see you here!",
                modifier = Modifier.padding(12.dp),
                fontSize = 16.sp
            )

        }

        val visible = remember {mutableStateOf(false)}

        if (finished || doRender) {
            visible.value = true
        } else {
            visible.value = false
        }

        AnimatedVisibility(
            visible.value,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            CustomCard(
                modifier = Modifier
                    .animateContentSize()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (doRender) {
                        Text(
                            text = "Rendering...",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 1.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            //horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxSize()
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(7.5f),
                            ) {

                                Progressbar( // Current pic
                                    modifier = Modifier,
                                    progressPercent = progressPercent.value
                                )
                                Spacer(Modifier.padding(10.dp))
                                Progressbar( // All pic
                                    modifier = Modifier,
                                    progressPercent = currentCount.intValue / count.intValue.toFloat()
                                )
                            }
                            Spacer(Modifier.padding(4.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(2.5f)
                            ) {
                                Text(
                                    text = "${"%.2f".format(if (progressPercent.value != null) progressPercent.value!! * 100f else 100f)}%",
                                )
                                Text(
                                    text = "${currentCount.intValue} / ${count.intValue}",
                                )
                            }

                        }

                    } else if (finished) {
                        if (stop) {
                            Text(
                                text = "Stopped!",
                                modifier = Modifier,
                                fontSize = 16.sp
                            )
                        } else {
                            Text(
                                text = "Finished!",
                                modifier = Modifier,
                                fontSize = 16.sp

                            )
                        }
                        Text(
                            text = "Path: $imagePath",
                            modifier = Modifier,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Runtime: $runtime ms",
                            modifier = Modifier,
                            fontSize = 16.sp
                        )
                    } else if (stop) {

                        Text(
                            text = "Runtime: $runtime ms",
                            modifier = Modifier,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }


        WelcomeScreenState(context, alpha, quality, format, width, height, count)



        Spacer(Modifier.padding(50.dp))
    }
}

@Composable
fun WelcomeScreenState(
    context: Context,
    alpha: MutableState<Boolean>,
    quality: MutableState<Int>,
    format: MutableState<String>,
    width: MutableState<Int>,
    height: MutableState<Int>,
    count: MutableState<Int>,
){
    val checked = remember { mutableStateOf(false) }
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
                val animationDuration: Int = 150




                Row {
                    AnimatedContent(
                        targetState = quality.value.toString()[0],
                        transitionSpec = {
                            counterAnimation(initialState, targetState, animationDuration, 0.15f, 0.15f).using(SizeTransform(clip = false))
                        },
                        label = "quality value"
                    ) { targetQuality ->
                        Text(text = "$targetQuality",
                            modifier = Modifier,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    AnimatedContent(
                        targetState = if (quality.value < 10) ' ' else quality.value.toString()[1],
                        transitionSpec = {
                            counterAnimation(initialState, targetState, animationDuration, 0.15f, 0.15f).using(SizeTransform(clip = false))
                        },
                        label = "quality value"
                    ) { targetQuality ->
                        Text(text = "$targetQuality",
                            modifier = Modifier,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    AnimatedContent(
                        targetState = if (quality.value < 100) ' ' else quality.value.toString()[2],
                        transitionSpec = {
                            counterAnimation(initialState, targetState, animationDuration, 0.15f, 0.15f).using(SizeTransform(clip = false))
                        },
                        label = "quality value"
                    ) { targetQuality ->
                        Text(text = "$targetQuality",
                            modifier = Modifier,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            val haptic = LocalHapticFeedback.current

            Slider(
                value = quality.value.toFloat(),
                onValueChange = {
                    val newValue = it.roundToInt()


                    if (/*quality.value % 10 == 0 &&*/ newValue != quality.value) {
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
                steps = 99,
            )

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
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = "Resolution",
                    modifier = Modifier,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    textAlign = TextAlign.Left
                )
                Text(text = "Width × Height in pixels.",
                    modifier = Modifier,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left
                )
            }
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
                    shape = MaterialTheme.shapes.small,
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
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .requiredWidth(100.dp)
                        .padding(start = 8.dp)
                )
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
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = "Number of images",
                    modifier = Modifier,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    textAlign = TextAlign.Left
                )
                Text(text = "Specify how many random images to generate",
                    modifier = Modifier,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left
                )
            }

            val countText = if (count.value == 0) remember { mutableStateOf("") } else remember { mutableStateOf(count.value.toString()) }
            OutlinedTextField(
                value = countText.value,
                onValueChange = {newCount ->
                    countText.value = newCount // For correct preview
                    count.value = newCount.toIntOrNull() ?: 0
                },
                label = { Text("count") },
                //placeholder = { Text(text = "enter width", modifier = Modifier.alpha(0.5F)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .requiredWidth(100.dp)
                    .padding(end = 8.dp)

            )
        }
    }



    //sendNotification(context, R.drawable.ic_launcher_monocrome, "Maaao", "Heyho", NotificationCompat.PRIORITY_MIN) // Test notification

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    CustomCard {
        Text("For testing purposes:\nareNotificationsEnabled() = ${notificationManager.areNotificationsEnabled()}")
    }

//    var shouldStop = false
//    var mao: MutableState<Float?> = remember { mutableStateOf<Float?>(null) }
//    LaunchedEffect(Unit) {
//        delay(2000)
//        mao.value = 0f
//
//        while (!shouldStop) {
//            delay(1000)
//            mao.value = mao.value!! + 0.1f
//            if (mao.value == 1f) {
//                mao.value = 0f
//                delay(10)
//            }
//        }
//    }
// Progressbar(progressPercent = mao.value)

}
@Composable
fun Progressbar(
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    progressPercent: Float? = null
) {
    val color: Color = MaterialTheme.colorScheme.primary
    val backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent ?: 0f, // If progresspercent == null, return 0f. Else, return progresspercent.
        animationSpec = tween(
            durationMillis = 500, // Duration of the animation
            easing = LinearOutSlowInEasing // Easing function for smooth effect
        ), label = "Progress animation"
    )

    Crossfade(targetState = progressPercent == null) { isIndeterminate ->
        if (isIndeterminate) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = color,
                trackColor = backgroundColor,
            )
        } else {
            LinearProgressIndicator(
                progress = { animatedProgress},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = color,
                trackColor = backgroundColor,
            )
        }
    }
}




