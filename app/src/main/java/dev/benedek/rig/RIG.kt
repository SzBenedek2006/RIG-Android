package dev.benedek.rig

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.util.Log
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import java.io.File
import java.io.FileOutputStream

class RIG() {

    fun randomImageGenerator(context: Context, progressPercent: MutableState<Float>, outputPath: String, width: Int, height: Int, alpha: Boolean, quality: Int, format: String, count: Int, currentCount: MutableIntState): Array<File> {

        var imagePaths = emptyArray<File>()


        // For loop and list for files. (to handle count and generate multiple images.)
        if (format == "PNG"){
            imagePaths = generatePng(width, height, outputPath, progressPercent, alpha, count, currentCount)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha}")
        } else if (format == "JPEG") {
            //imagePath = generateJPEG(width, height, outputPath, progressPercent, quality, count, currentCount)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha}")
        } else {
            imagePaths = emptyArray()
        }

        return imagePaths

    }



    fun generatePng(
        width: Int,
        height: Int,
        outputPath: String,
        progressPercent: MutableState<Float>,
        alpha: Boolean,
        count: Int,
        currentCount: MutableIntState,
    ): Array<File> {

        val format = "png"
        var file = Array<File>(count){ File("${outputPath}/image${it}.$format") }

        for (num in 0 until count) {

            currentCount.intValue = num + 1

            val bitmap = if (alpha){
                genBitmapAlpha(width, height, progressPercent)
            } else {
                genBitmap(width, height, progressPercent)
            }



            // Saving bitmap as a PNG
            file[num] = File("${outputPath}/image${num+1}.$format")


            // Create a file output stream to save the bitmap
            FileOutputStream(file[num]).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Save as PNG
            }
        }
        return file

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