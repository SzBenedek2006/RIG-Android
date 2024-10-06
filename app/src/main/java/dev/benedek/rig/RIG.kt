package dev.benedek.rig

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import java.io.File
import java.io.FileOutputStream

class RIG() {

    fun randomImageGenerator(context: Context, progressPercent: MutableState<Float>, outputPath: String, width: Int, height: Int, alpha: MutableState<Boolean>, quality): String {

        var imagePath: String
        if (alpha.value) {
            imagePath = generatePngAlpha(width, height, outputPath, progressPercent, quality)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha.value}")
        } else {
            imagePath = generatePng(width, height, outputPath, progressPercent, quality)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha.value}")
        }

        return imagePath // Return the path of the saved image
    }

    fun generatePngAlpha(width: Int, height: Int, outputPath: String, progressPercent: MutableState<Float>, quality): String {
        val bitmap = genBitmapAlpha(width, height, progressPercent)
        return saveBitmapAsPNG(bitmap, outputPath, quality)
    }

    fun generatePng(width: Int, height: Int, outputPath: String, progressPercent: MutableState<Float>, quality): String {
        val bitmap = genBitmap(width, height, progressPercent)
        return saveBitmapAsPNG(bitmap, outputPath, quality)
    }

    fun saveBitmapAsPNG(bitmap: Bitmap, outputPath: String, quiality: Int): String {
        val file = File(outputPath)

        // Create a file output stream to save the bitmap
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Save as PNG
        }

        return file.absolutePath // Return the absolute path of the saved file
    }

    fun saveBitmapAsJPEG(bitmap: Bitmap, outputPath: String, quiality: Int) {
        val file = File(outputPath)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // Save as PNG
        }
    }


}