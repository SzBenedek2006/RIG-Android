@file:OptIn(ExperimentalMaterial3Api::class)

package dev.benedek.rig

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.benedek.rig.ui.theme.RIGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val rig = RIG(this)
        val savedImagePath = rig.randomImageGenerator()
        setContent {
            RIGTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
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
                        path = savedImagePath,
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun SmallTopAppBarExample() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("RIG for android")
                }
            )
        },
    ) { innerPadding ->
        ScrollContent(innerPadding)
    }
}


@Composable
fun RigUi(path: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ){
        Column (
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(
                modifier = modifier
                    .padding(top = 32.dp),
                text = "Say hello to RIG!",
                //lineHeight = 8.sp,
                fontSize = 36.sp
            )
            Text(
                text = "Nice to see you here!",
                modifier = modifier,
                //lineHeight = 8.sp,
                fontSize = 16.sp
            )
            Text(
                text = "Generated path: $path",
                modifier = modifier,
                //lineHeight = 8.sp,
                fontSize = 16.sp
            )

        }
    }
}

@Composable
fun ScrollContent(innerPadding: PaddingValues) {
    val range = 1..100

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(range.count()) { index ->
            Text(text = "- List item number ${index + 1}")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, name = "Main screen")
@Composable
fun GreetingPreview() {
        RIGTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                RigUi(
                    path = "RIG",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
}