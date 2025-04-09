package dev.benedek.rig

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class RIG {

    fun randomImageGenerator(
        context: Context,
        mainScreenViewModel: MainScreenViewModel,
        outputPath: String,
    ): Array<File> {

        var imagePaths = emptyArray<File>()

        val notificationThread = Thread {
            do {
                sendNotification(context = context, title = "Generating image...", text = "${mainScreenViewModel.progressPercent?.times(100)}%", isSilent = true)
                Log.d("RIG", "sendNotification triggered")
                Thread.sleep(500)
            } while (mainScreenViewModel.progressPercent != null && imagePaths.contentEquals(emptyArray<File>()))
            sendNotification(context = context, title = "Finished rendering.", text = "")

        }.start()

        // For loop and list for files. (to handle count and generate multiple images.)
        if (mainScreenViewModel.format == "PNG"){
            imagePaths = generatePng(mainScreenViewModel, outputPath)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${mainScreenViewModel.alpha}")
        } else if (mainScreenViewModel.format == "JPEG") {
            imagePaths = generateJPEG(mainScreenViewModel, outputPath)
            Log.d("RIG", "Context: ${context::class.java.simpleName}, Alpha: ${mainScreenViewModel.alpha}")
        } else {
            imagePaths = emptyArray()
        }

        return imagePaths

    }



    fun generatePng(
        mainScreenViewModel: MainScreenViewModel,
        outputPath: String,
    ): Array<File> {

        val format = "png"
        var file = Array<File>(mainScreenViewModel.count){ File("${outputPath}/image${it}.$format") }

        for (num in 0 until mainScreenViewModel.count) {

            if (mainScreenViewModel.stop) {
                Log.d("RIG", "PNG gen interrupted")
                return file
            }

            mainScreenViewModel.updateCurrentCount(num + 1)

            val bitmap = if (mainScreenViewModel.alpha){
                genBitmapAlpha(mainScreenViewModel)
            } else {
                genBitmap(mainScreenViewModel)
            }
            mainScreenViewModel.updateProgressPercent(null)

            file[num] = File("${outputPath}/image${num+1}.$mainScreenViewModel.format")

            FileOutputStream(file[num]).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Save as PNG
            }
        }

        return file

    }

    fun generateJPEG(
        mainScreenViewModel: MainScreenViewModel,
        outputPath: String,
    ): Array<File> {
        val format = "png"
        var file = Array<File>(mainScreenViewModel.count){ File("${outputPath}/image${it}.$mainScreenViewModel.format") }



        for (num in 0 until mainScreenViewModel.count) {

            if (mainScreenViewModel.stop) {
                Log.d("RIG", "JPEG gen interrupted")
                return file
            }

            mainScreenViewModel.updateCurrentCount(num + 1)

            val bitmap = genBitmap(mainScreenViewModel)
            mainScreenViewModel.updateProgressPercent(null)

            file[num] = File("${outputPath}/image${num+1}.$mainScreenViewModel.format")

            FileOutputStream(file[num]).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, mainScreenViewModel.quality, outputStream) // Save as PNG
            }
        }
        return file
    }
}