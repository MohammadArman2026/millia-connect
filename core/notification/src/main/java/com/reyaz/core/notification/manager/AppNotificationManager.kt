package com.reyaz.core.notification.manager

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.reyaz.core.notification.model.NotificationData
import com.reyaz.core.notification.utils.createNotificationChannel

class AppNotificationManager(
    private val context: Context
) {
    private fun getIntent(uri: Uri): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(notificationData: NotificationData) {
        createNotificationChannel(
            context,
            notificationData.channelId,
            channelName = notificationData.channelName,
            importance = notificationData.importance
        )
        val notification = NotificationCompat.Builder(context, notificationData.channelId)
            .setContentTitle(notificationData.title)
            .setContentTitle(notificationData.title)
            .setSilent(notificationData.importance != NotificationCompat.PRIORITY_MIN)
            .setContentText(notificationData.message)
            .setSmallIcon(
                notificationData.iconResId ?: com.reyaz.core.ui.R.drawable.notification_icon
            )
            .setPriority(notificationData.priority)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationData.message))
            .setContentIntent(getIntent(uri = notificationData.destinationUri))
            .build()
        NotificationManagerCompat.from(context).notify(notificationData.id, notification)
    }
}