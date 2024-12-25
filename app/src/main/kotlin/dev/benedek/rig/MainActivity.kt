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

        GlobalScope.launch(Dispatchers.Default) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }


        }



        enableEdgeToEdge()
        setContent {
            RIGTheme(
                darkTheme = darkThemeToggle.value,
                dynamicColor = dynamicColorToggle.value
            ) {
                MyApp(darkThemeToggle)
            }
        }
    }
}

@Composable
fun MyApp(darkThemeToggle: MutableState<Boolean>) {
    val context = LocalContext.current
    createNotificationChannel(context)

    // For retrieving the state of the dark mode toggle


    val darkModeFlow: Flow<Boolean> = remember { restoreThemeToggle(context = context) }

    darkThemeToggle.value = darkModeFlow.collectAsState(initial = true).value // only works with the same value as initialised in mutablestate

    if (followSystemTheme.value) {
        darkThemeToggle.value = isSystemInDarkTheme()
    }


    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("settings") { SettingsScreen(navController, darkThemeToggle) } // Settings Screen
    }
}






@Preview(showBackground = true, showSystemUi = true, name = "Main screen")
@Composable
fun RigUIPreview() {


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
                finished = false,
                doRender = false,
                stop = false,
                progressPercent = remember { mutableFloatStateOf(0f) },
                alpha = remember { mutableStateOf(false) },
                quality = remember { mutableIntStateOf(100) },
                format = remember { mutableStateOf("PNG") },
                width = remember { mutableIntStateOf(0) },
                height = remember { mutableIntStateOf(0) },
                count = remember { mutableIntStateOf(10) },
                currentCount = remember { mutableIntStateOf(1) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
