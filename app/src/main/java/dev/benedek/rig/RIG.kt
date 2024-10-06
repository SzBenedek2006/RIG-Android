package dev.benedek.rig

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.util.Log
import androidx.compose.runtime.MutableState
import java.io.File
import java.io.FileOutputStream

class RIG() {

    fun randomImageGenerator(context: Context, progressPercent: MutableState<Float>, outputPath: String, width: Int, height: Int, alpha: Boolean, quality: Int, format: String): String {

        var imagePath: String
        if (alpha && format == "PNG") {
            imagePath = generatePngAlpha(width, height, outputPath, progressPercent, quality)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha}")
        } else {
            if (format == "PNG"){
                imagePath = generatePng(width, height, outputPath, progressPercent, quality)
                Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha}")
            } else if (format == "JPEG") {
                imagePath = generateJPEG(width, height, outputPath, progressPercent, quality)
                Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha}")
            } else {
                imagePath = ""
            }
        }

        return imagePath // Return the path of the saved image
    }

    fun generatePngAlpha(
        width: Int,
        height: Int,
        outputPath: String,
        progressPercent: MutableState<Float>,
        quality: Int
    ): String {
        val bitmap = genBitmapAlpha(width, height, progressPercent)
        return saveBitmapAsPNG(bitmap, outputPath, quality)
    }

    fun generatePng(
        width: Int,
        height: Int,
        outputPath: String,
        progressPercent: MutableState<Float>,
        quality: Int
    ): String {
        val bitmap = genBitmap(width, height, progressPercent)
        return saveBitmapAsPNG(bitmap, outputPath, quality)
    }

    fun generateJPEG(width: Int, height: Int, outputPath: String, progressPercent: MutableState<Float>, quality: Int): String {
        val bitmap = genBitmap(width, height, progressPercent)
        return saveBitmapAsJPEG(bitmap, outputPath, quality)
    }

    fun saveBitmapAsPNG(bitmap: Bitmap, outputPath: String, quality: Int): String {
        val file = File(outputPath)

        // Create a file output stream to save the bitmap
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream) // Save as PNG
        }

        return file.absolutePath // Return the absolute path of the saved file
    }

    fun saveBitmapAsJPEG(bitmap: Bitmap, outputPath: String, quality: Int): String {
        val file = File(outputPath)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream) // Save as PNG
        }
        return file.absolutePath
    }
}