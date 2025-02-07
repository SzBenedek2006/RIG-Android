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
    icon: Int = R.drawable.ic_launcher_monocrome,
    title: String,
    text: String,
    priority: Int = NotificationManager.IMPORTANCE_DEFAULT,
    isSilent: Boolean = false
) {

    GlobalScope.launch(Dispatchers.Default) {

        val notificationId = 1

        val notification = NotificationCompat.Builder(context, "mao")
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(priority)
            .setSilent(isSilent)
            .build()

        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.notify(notificationId, notification)


    }


}

