package com.reyaz.milliaconnect1.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.reyaz.milliaconnect1.R
import com.reyaz.milliaconnect1.MainActivity // Import your main activity

class NotificationHelper(private val applicationContext: Context) {

    fun showNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "auto_connect_channel",
                "Auto Connect Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for auto connect status"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create an intent to launch MainActivity when notification is clicked
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Create a PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification with the pendingIntent
        val notification = NotificationCompat.Builder(applicationContext, "auto_connect_channel")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)  // Add this line to make notification clickable
            .setAutoCancel(true)              // Automatically remove notification when clicked
            .build()

        // Show the notification
        notificationManager.notify(1, notification)
    }
}