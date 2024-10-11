package dev.benedek.rig

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.util.Log
import androidx.compose.runtime.MutableState
import java.io.File
import java.io.FileOutputStream

class RIG() {

    fun randomImageGenerator(context: Context, progressPercent: MutableState<Float>, outputPath: String, width: Int, height: Int, alpha: Boolean, quality: Int, format: String, count: Int): String {

        var imagePath: String


        if (format == "PNG"){
            imagePath = generatePng(width, height, outputPath, progressPercent, alpha, count)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha}")
        } else if (format == "JPEG") {
            imagePath = generateJPEG(width, height, outputPath, progressPercent, quality, count)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha}")
        } else {
            imagePath = ""
        }


        return imagePath // Return the absolute path of the saved image
    }



    fun generatePng(
        width: Int,
        height: Int,
        outputPath: String,
        progressPercent: MutableState<Float>,
        alpha: Boolean,
        count: Int
    ): String {

        val bitmap = if (alpha){
            genBitmapAlpha(width, height, progressPercent)
        } else {
            genBitmap(width, height, progressPercent)
        }

        // Saving bitmap as a PNG
        val file = File(outputPath)
        // Create a file output stream to save the bitmap
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Save as PNG
        }

        return file.absolutePath // Return the absolute path of the saved file
    }

    fun generateJPEG(
        width: Int,
        height: Int,
        outputPath: String,
        progressPercent: MutableState<Float>,
        quality: Int,
        count: Int
    ): String {
        val bitmap = genBitmap(width, height, progressPercent)

        // Saving bitmap as a JPEG
        val file = File(outputPath)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream) // Save as PNG
        }
        return file.absolutePath
    }


}