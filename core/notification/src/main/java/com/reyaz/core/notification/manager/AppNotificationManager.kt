package com.reyaz.core.notification.manager

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.reyaz.core.notification.R
import com.reyaz.core.notification.model.NotificationData
import com.reyaz.core.notification.utils.createNotificationChannel

class AppNotificationManager(
    private val context: Context
) {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(notificationData: NotificationData) {
        createNotificationChannel(
            context,
            notificationData.channelId,
            channelName = notificationData.channelName
        )
        val notification = NotificationCompat.Builder(context, notificationData.channelId)
            .setContentTitle(notificationData.title)
            .setContentText(notificationData.message)
            .setSmallIcon(notificationData.iconResId ?: R.drawable.ic_notification)
            .setPriority(notificationData.priority)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
        NotificationManagerCompat.from(context).notify(notificationData.id, notification)
    }
}