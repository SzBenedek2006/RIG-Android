package dev.benedek.rig

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.runtime.MutableState
import java.util.Random


fun genBitmap(width: Int, height: Int, progressPercent: MutableState<Float?>): Bitmap {
    val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565) // Alpha is first
    var progress = 0f
    val random = Random()
    for (x in 0 until width) {
        for (y in 0 until height) {
            // Generate random colors (without alpha for RGB_565)
            val randomColor = Color.rgb(
                random.nextInt(256), // Red
                random.nextInt(256), // Green
                random.nextInt(256)  // Blue
            )
            bitmap.setPixel(x, y, randomColor)

        }
        progress++
        progressPercent.value = (progress / width)
    }
    return bitmap
}