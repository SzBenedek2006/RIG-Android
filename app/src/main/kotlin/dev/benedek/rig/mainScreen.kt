package dev.benedek.rig

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController

import kotlin.math.pow
import kotlin.math.roundToInt
import android.app.NotificationManager as NotificationManager



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    // https://developer.android.com/develop/ui/compose/components/scaffold


    val mainScreenViewModel = viewModel<MainScreenViewModel>()


    val context = LocalContext.current

    val outputPath = context.filesDir.absolutePath


    val rigThread = Thread {
        mainScreenViewModel.updateStop(false)
        val firstTime = System.currentTimeMillis()
        mainScreenViewModel.updateFinished(false)
        mainScreenViewModel.updateProgressPercent(0f)
        val rig = RIG()


        rig.randomImageGenerator(
            context,
            mainScreenViewModel,
            outputPath,
        )



        val secondTime = System.currentTimeMillis()
        runtime = secondTime - firstTime
        mainScreenViewModel.updateProgressPercent(0f)
        mainScreenViewModel.updateDoRender(false)
        mainScreenViewModel.updateFinished(true)
    }

    if (mainScreenViewModel.doRender) {
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
                        IconButton(onClick = { mainScreenViewModel.updateStop(true) }) {
                            Icon(
                                imageVector = Icons.Sharp.Clear,
                                contentDescription = "Clear images"
                            )
                        }
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) } ) { // Navigate to settings
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
                    mainScreenViewModel.incrementPresses()
                    if (mainScreenViewModel.width > 0 && mainScreenViewModel.height > 0 && mainScreenViewModel.count > 0) {
                        mainScreenViewModel.updateDoRender(true)
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
            mainScreenViewModel.finished,
            mainScreenViewModel.doRender,
            mainScreenViewModel.stop,
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
            mainScreenViewModel
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
    modifier: Modifier = Modifier,
    mainScreenViewModel: MainScreenViewModel
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

        val visible = remember { mutableStateOf(false) }

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
                                    progressPercent = mainScreenViewModel.progressPercent
                                )
                                Spacer(Modifier.padding(10.dp))
                                Progressbar( // All pic
                                    modifier = Modifier,
                                    progressPercent = mainScreenViewModel.currentCount / mainScreenViewModel.count.toFloat()
                                )
                            }
                            Spacer(Modifier.padding(4.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(2.5f)
                            ) {
                                Text(
                                    text = "${"%.2f".format(if (mainScreenViewModel.progressPercent != null) mainScreenViewModel.progressPercent!! * 100f else 100f)}%",
                                )
                                Text(
                                    text = "${mainScreenViewModel.currentCount} / ${mainScreenViewModel.count}",
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


        WelcomeScreenState(
            context,
            mainScreenViewModel = mainScreenViewModel
        )



        Spacer(Modifier.padding(50.dp))
    }
}

@Composable
fun WelcomeScreenState(
    context: Context,
    mainScreenViewModel: MainScreenViewModel
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
                checked = mainScreenViewModel.alphaState,
                enabled = mainScreenViewModel.format == "PNG",
                onCheckedChange = {
                    mainScreenViewModel.toggleAlphaSwitch(it)
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
                        targetState = mainScreenViewModel.quality.toString()[0],
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
                        targetState = if (mainScreenViewModel.quality < 10) ' ' else mainScreenViewModel.quality.toString()[1],
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
                        targetState = if (mainScreenViewModel.quality < 100) ' ' else mainScreenViewModel.quality.toString()[2],
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
                value = mainScreenViewModel.quality.toFloat(),
                onValueChange = {
                    val newValue = it.roundToInt()


                    if (/*mainScreenViewModel.quality % 10 == 0 &&*/ newValue != mainScreenViewModel.quality) {
                        mainScreenViewModel.updateQuality(newValue)

                        if (mainScreenViewModel.quality != 100 && mainScreenViewModel.quality != 0) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        } else {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }


                                },
                enabled = mainScreenViewModel.format == "JPEG",
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
                val pngToggled = if (mainScreenViewModel.format == "PNG") remember { mutableStateOf(true) } else remember { mutableStateOf(false) }
                val jpegToggled = if (mainScreenViewModel.format == "JPEG") remember { mutableStateOf(true) } else remember { mutableStateOf(false) }


                formatButton("PNG", pngToggled, jpegToggled, Modifier.padding(3.dp))
                formatButton("JPEG", jpegToggled, pngToggled, Modifier.padding(3.dp))

                mainScreenViewModel.setFormatTo(if (pngToggled.value) "PNG" else if (jpegToggled.value) "JPEG" else "")

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

                val widthText = if (mainScreenViewModel.width == 0) remember { mutableStateOf("") } else remember { mutableStateOf(mainScreenViewModel.width.toString()) }
                OutlinedTextField(
                    value = widthText.value,
                    onValueChange = {newWidth ->
                        widthText.value = newWidth // For correct preview
                        mainScreenViewModel.updateWidth(newWidth.toIntOrNull() ?: 0)
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
                val heightText = if (mainScreenViewModel.height == 0) remember { mutableStateOf("") } else remember { mutableStateOf(mainScreenViewModel.height.toString()) }
                OutlinedTextField(
                    value = heightText.value,
                    onValueChange = {newHeight ->
                        heightText.value = newHeight // For correct preview
                        mainScreenViewModel.updateHeight(newHeight.toIntOrNull() ?: 0)
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

            val countText = if (mainScreenViewModel.count == 0) remember { mutableStateOf("") } else remember { mutableStateOf(mainScreenViewModel.count.toString()) }
            OutlinedTextField(
                value = countText.value,
                onValueChange = {newCount ->
                    countText.value = newCount // For correct preview
                    mainScreenViewModel.updateCount(newCount.toIntOrNull() ?: 0)
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
            durationMillis = 250, // Duration of the animation
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




