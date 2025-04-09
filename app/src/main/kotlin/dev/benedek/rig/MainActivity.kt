@file:OptIn(ExperimentalMaterial3Api::class)

package dev.benedek.rig

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.benedek.rig.ui.theme.RIGTheme
import kotlinx.coroutines.flow.Flow
import java.io.File
import android.Manifest
import android.content.res.Resources
import android.window.SplashScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


var imagePath = emptyArray<File>()
var runtime: Long = 0

val dynamicColorToggle = mutableStateOf( true )
val followSystemTheme = mutableStateOf( false )


class MainActivity : ComponentActivity() {
    val darkThemeToggle = mutableStateOf(false)
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }



        setContent {
            AppTheme {
                AppNavigation(
                    darkThemeToggle = darkThemeToggle
                )
            }
        }
    }
}
// new composables starts here
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val darkTheme = rememberThemeState(context)

    RIGTheme(
        darkTheme = darkTheme.value,
        dynamicColor = dynamicColorToggle.value
    ) {
        content()
    }
}

// 5. Simplified theme state management
@Composable
fun rememberThemeState(context: Context): MutableState<Boolean> {
    val darkTheme = remember { mutableStateOf(false) }
    val darkModeFlow = remember { restoreThemeToggle(context) }

    LaunchedEffect(darkModeFlow) {
        darkModeFlow.collect { darkTheme.value = it }
    }

    if (followSystemTheme.value) {
        darkTheme.value = isSystemInDarkTheme()
    }

    return darkTheme
}


// new composables end here




@Composable
fun AppNavigation(darkThemeToggle: MutableState<Boolean>) {
    val context = LocalContext.current
    createNotificationChannel(context)

    // For retrieving the state of the dark mode toggle
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController, darkThemeToggle)
        }
    }

}


