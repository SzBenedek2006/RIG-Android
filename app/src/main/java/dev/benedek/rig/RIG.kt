package dev.benedek.rig

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class RIG(private val context: Context) {
    fun randomImageGenerator(): String {
        val firstTime = System.currentTimeMillis()

        val outputPath = context.filesDir.absolutePath + "image.png"
        val width = 1000
        val height = 1000
        val count = 1
        var image: Image
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) // Alpha is first

        for (x in 0 until width) {
            for (y in 0 until height) {
                // Generate random colors (without alpha for RGB_565)
                val randomColor = Color.argb(
                    Random.nextInt(256), // Alpha
                    Random.nextInt(256), // Red
                    Random.nextInt(256), // Green
                    Random.nextInt(256)  // Blue
                )
                bitmap.setPixel(x, y, randomColor)
            }
        }


        val imagePath = saveBitmapAsPng(bitmap, outputPath)



        val secondTime = System.currentTimeMillis()
        val runtime = secondTime - firstTime

        return "Generated image at:\n" + imagePath + "\nin $runtime ms." // Return the path of the saved image
    }

    private fun saveBitmapAsPng(bitmap: Bitmap, outputPath: String): String {
        val file = File(outputPath)

        // Create a file output stream to save the bitmap
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Save as PNG
        }

        return file.absolutePath // Return the absolute path of the saved file
    }
}