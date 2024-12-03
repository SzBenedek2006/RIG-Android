package dev.benedek.rig

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun CustomCard(
    modifier: Modifier = Modifier.fillMaxWidth(),
    //onClick: (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Surface(
        modifier = modifier
            //.clickable(onClick = onClick ?: {})
            .padding(8.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, Color.Gray),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}
@Composable
fun formatButton(text: String, isToggled: MutableState<Boolean>, invalidate: MutableState<Boolean>, modifier: Modifier): Boolean { // Make invalidate parameter number flexible
    val haptic = LocalHapticFeedback.current

    if (isToggled.value) {
        Button(
            onClick = {
                if (isToggled.value) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }


            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = modifier,
        ) {
            Text(text)
        }
        return true
    } else {
        OutlinedButton(
            onClick = {
                isToggled.value = !isToggled.value // Toggle the color state
                invalidate.value = false
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = modifier,
        ) {
            Text(text)
        }
        return false
    }


}

fun counterAnimation(
    initialState: Char,
    targetState: Char,
    animationDuration: Int,
    heightScale: Float, // = 0.25f
    initialAlpha: Float, // = 0.25f
): ContentTransform {

    return if (targetState < initialState && !(initialState == '9' && targetState == '0') && !(initialState == '9' && targetState == '1') ||
        (initialState == '1' && targetState == '9') ||
        (initialState == '0' && targetState == '9')
    ) {
        slideInVertically(animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing)) { height -> (height * (heightScale/4)).toInt() } +
                fadeIn(animationSpec = tween(durationMillis = animationDuration / 2), initialAlpha = initialAlpha) togetherWith
                slideOutVertically(animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing)) { height -> -(height * (heightScale*2)).toInt() } +
                fadeOut(animationSpec = tween(durationMillis = animationDuration), targetAlpha = initialAlpha)
    } else {
        slideInVertically(animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing)) { height -> -(height * (heightScale/4)).toInt() } +
                fadeIn(animationSpec = tween(durationMillis = animationDuration / 2), initialAlpha = initialAlpha) togetherWith
                slideOutVertically(animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing)) { height -> (height * (heightScale*2)).toInt() } +
                fadeOut(animationSpec = tween(durationMillis = animationDuration))
    }
}