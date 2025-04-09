package dev.benedek.rig

import android.graphics.Bitmap
import android.graphics.Color
import java.util.Random
import androidx.core.graphics.set
import androidx.core.graphics.createBitmap


fun genBitmapAlpha(mainScreenViewModel: MainScreenViewModel): Bitmap {
    val bitmap: Bitmap = createBitmap(mainScreenViewModel.width, mainScreenViewModel.height) // Alpha is first
    var progress = 0
    val random = Random()
    for (x in 0 until mainScreenViewModel.width) {
        for (y in 0 until mainScreenViewModel.height) {
            // Generate random colors (without alpha for RGB_565)
            val randomColor = Color.argb(
                random.nextInt(256), // Alpha
                random.nextInt(256), // Red
                random.nextInt(256), // Green
                random.nextInt(256)  // Blue
            )
            bitmap[x, y] = randomColor

        }
        progress++
        mainScreenViewModel.updateProgressPercent(progress.toFloat() / mainScreenViewModel.width)
    }
    return bitmap
}