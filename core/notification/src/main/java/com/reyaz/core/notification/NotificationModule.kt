package com.reyaz.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.reyaz.core.notification.manager.AppNotificationManager
import org.koin.dsl.module

val notificationModule = module {
    single { AppNotificationManager(get()) }
    /*single {
        NotificationCompat.Builder(get(), "My_CHANNEL_ID")
            .setContentTitle("My Title")
            .setContentText("My Description")
            .setSmallIcon(androidx.constraintlayout.widget.R.drawable.abc_ic_star_half_black_16dp)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

    single {
        val notificationManager = NotificationManagerCompat.from(get())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "My_CHANNEL_ID",
                "My Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager
    }*/
}