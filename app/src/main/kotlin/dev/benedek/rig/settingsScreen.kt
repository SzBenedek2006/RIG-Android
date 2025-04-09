package dev.benedek.rig

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter



val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

fun saveThemeToggle(context: Context, darkThemeToggle: MutableState<Boolean>) {
    runBlocking {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = darkThemeToggle.value
        }
    }
}

fun restoreThemeToggle(context: Context) = context.dataStore.data.map { preferences ->
    preferences[DARK_MODE_KEY] ?: false // Default to false
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    darkThemeToggle: MutableState<Boolean>
) {



    val focusManager = LocalFocusManager.current

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
            darkThemeToggle
        )
    }

}




@Composable
fun SettingsUi(modifier: Modifier, darkThemeToggle: MutableState<Boolean>) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

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
            CustomCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Theme",
                            modifier = Modifier,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            textAlign = TextAlign.Center
                        )
                        if (darkThemeToggle.value) {
                            Text(
                                text = "Welcome to the dark side",
                                modifier = Modifier,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = "Snow white",
                                modifier = Modifier,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                textAlign = TextAlign.Center
                            )
                        }

                    }

                    Switch(
                        enabled = !followSystemTheme.value,
                        checked = darkThemeToggle.value,
                        onCheckedChange = {
                            darkThemeToggle.value = it
                            saveThemeToggle(context, darkThemeToggle)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )
                }
            }

            CustomCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Follow system theme",
                            modifier = Modifier,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            textAlign = TextAlign.Center
                        )
                        if (followSystemTheme.value) {
                            Text(
                                text = "Turned on",
                                modifier = Modifier,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = "Turned off",
                                modifier = Modifier,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                textAlign = TextAlign.Center
                            )
                        }

                    }

                    Switch(
                        checked = followSystemTheme.value,
                        onCheckedChange = {
                            followSystemTheme.value = it
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )
                }
            }

            CustomCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Material You",
                            modifier = Modifier,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            textAlign = TextAlign.Center
                        )
                        if (dynamicColorToggle.value) {
                            Text(
                                text = "I like this color too!",
                                modifier = Modifier,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = "RIG™ color.",
                                modifier = Modifier,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                textAlign = TextAlign.Center
                            )
                        }

                    }

                    Switch(
                        checked = dynamicColorToggle.value,
                        onCheckedChange = {
                            dynamicColorToggle.value = it
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )
                }
            }


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

