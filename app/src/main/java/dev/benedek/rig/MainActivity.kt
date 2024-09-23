package dev.benedek.rig

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
        setContent {
            RIGTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RigUi(
                        name = "RIG",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun RigUi(name: String, modifier: Modifier = Modifier) {
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
                text = "Say hello to $name!",
                //lineHeight = 8.sp,
                fontSize = 36.sp
            )
            Text(
                text = "Nice to see you here!",
                modifier = modifier,
                //lineHeight = 8.sp,
                fontSize = 16.sp,
            )
        }
        
    }

}

@Preview(showBackground = true, showSystemUi = true, name = "Main screen")
@Composable
fun GreetingPreview() {
        RIGTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                RigUi(
                    name = "RIG",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
}