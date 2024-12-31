@file:OptIn(DelicateCoroutinesApi::class)

package dev.benedek.rig

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@SuppressLint("ObsoleteSdkInt")
fun createNotificationChannel(
    context: Context,
    name: String = "Default",
    description: String = "RIG notifications",
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {

    GlobalScope.launch(Dispatchers.Default) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel("mao", name, importance).apply {}
            val notificationManager = context.getSystemService<NotificationManager>()
            notificationManager?.createNotificationChannel(channel)

        }

    }

}


fun sendNotification(
    context: Context,
    icon: Int,
    title: String,
    text: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {

    GlobalScope.launch(Dispatchers.Default) {

        val notificationId = 1 // Unique ID for the notification

        val notification = NotificationCompat.Builder(context, "mao")
            .setSmallIcon(R.drawable.ic_launcher_monocrome) // Use a valid drawable resource ID here
            .setContentTitle("Maaao!")
            .setContentText("Heyho!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.notify(notificationId, notification)


    }


}

