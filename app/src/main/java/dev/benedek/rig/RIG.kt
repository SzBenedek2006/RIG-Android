package dev.benedek.rig

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.Snapshot
import java.io.File
import java.io.FileOutputStream

class RIG() {

    fun randomImageGenerator(context: Context, progressPercent: MutableState<Float>, outputPath: String, width: Int, height: Int, alpha: Boolean, quality: Int, format: String, count: Int, currentCount: MutableIntState, stop: MutableState<Boolean>): Array<File> {

        var imagePaths = emptyArray<File>()


        // For loop and list for files. (to handle count and generate multiple images.)
        if (format == "PNG"){
            imagePaths = generatePng(width, height, outputPath, progressPercent, alpha, count, currentCount, stop)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${alpha}")
        } else if (format == "JPEG") {
            imagePaths = generateJPEG(width, height, outputPath, progressPercent, quality, count, currentCount, stop)
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
        stop: MutableState<Boolean>
    ): Array<File> {

        val format = "png"
        var file = Array<File>(count){ File("${outputPath}/image${it}.$format") }

        val pngThread = Thread {
            for (num in 0 until count) {

                if (stop.value) {
                    Log.d("RIG", "Png thread interrupted")
                    return@Thread
                }
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
        }
        pngThread.start()






        pngThread.join()
        return file

    }

    fun generateJPEG(
        width: Int,
        height: Int,
        outputPath: String,
        progressPercent: MutableState<Float>,
        quality: Int,
        count: Int,
        currentCount: MutableIntState,
        stop: MutableState<Boolean>
    ): Array<File> {
        val format = "png"
        var file = Array<File>(count){ File("${outputPath}/image${it}.$format") }


        // for (int i = 0; i <= 10; i++)
        for (num in 0 until count) {

            currentCount.intValue = num + 1

            val bitmap = genBitmap(width, height, progressPercent)



            // Saving bitmap as a PNG
            file[num] = File("${outputPath}/image${num+1}.$format")


            // Create a file output stream to save the bitmap
            FileOutputStream(file[num]).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream) // Save as PNG
            }
        }
        return file
    }
}