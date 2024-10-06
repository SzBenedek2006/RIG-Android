package dev.benedek.rig

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import androidx.compose.runtime.MutableState
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class RIG() {

    fun randomImageGenerator(progressPercent: MutableState<Float>, outputPath: String, width: Int, height: Int, alpha: MutableState<Boolean>): String {

        var imagePath: String
        if (alpha.value) {
            imagePath = generatePngAlpha(width, height, outputPath, progressPercent)
        } else {
            imagePath = generatePng(width, height, outputPath, progressPercent)
        }

        return imagePath // Return the path of the saved image
    }

    fun generatePngAlpha(width: Int, height: Int, outputPath: String, progressPercent: MutableState<Float>): String {
        val bitmap = genBitmapAlpha(width, height, progressPercent)
        return saveBitmapAsPng(bitmap, outputPath)
    }

    fun generatePng(width: Int, height: Int, outputPath: String, progressPercent: MutableState<Float>): String {
        val bitmap = genBitmap(width, height, progressPercent)
        return saveBitmapAsPng(bitmap, outputPath)
    }

    fun saveBitmapAsPng(bitmap: Bitmap, outputPath: String): String {
        val file = File(outputPath)

        // Create a file output stream to save the bitmap
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Save as PNG
        }

        return file.absolutePath // Return the absolute path of the saved file
    }

}