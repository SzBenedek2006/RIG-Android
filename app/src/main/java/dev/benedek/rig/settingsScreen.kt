package dev.benedek.rig

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    val transitionState = remember { MutableTransitionState(false) }
    LaunchedEffect(Unit) {
        transitionState.targetState = true // Trigger animation when the screen is displayed
    }


    val backStackEntry = navController.currentBackStackEntryAsState()

    // Trigger exit animation on back navigation
    if (backStackEntry.value?.destination?.route != "settings") {
        transitionState.targetState = false
    }



    val focusManager = LocalFocusManager.current

    AnimatedVisibility(
        visibleState = transitionState,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 300),
            initialOffsetX = { it } // Start from right
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 300),
            targetOffsetX = { it } // Slide out to the right
        )
    ) {
        Scaffold(
            topBar = {
                Surface {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        title = {
                            Text("Settings")
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) { // Navigate to settings
                                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back button")
                            }
                        }
                    )
                }
            },



            ) { innerPadding ->
            SettingsUi(

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


}

@Composable
fun SettingsUi(modifier: Modifier) {
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
                text = "This is RIG settings!",
                lineHeight = 42.sp,
                fontSize = 36.sp
            )
            Text(
                text = "Nice to see you here too!",
                modifier = Modifier.padding(12.dp),
                fontSize = 16.sp
            )

        }









        Spacer(Modifier.padding(50.dp))
        val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        val buildDate = context.packageManager.getPackageInfo(context.packageName, 0).lastUpdateTime

        val formattedBuildDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
            .format(Instant.ofEpochMilli(buildDate))



        Text(
            text = "Version: $version, Build date: $formattedBuildDate",
            modifier = Modifier
                .padding(12.dp)
                .alpha(0.5f),
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
    }
}

