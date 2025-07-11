package com.reyaz.core.notification.model

import android.app.NotificationManager
import android.net.Uri
import androidx.core.app.NotificationCompat

data class NotificationData(
    val id: Int,
    val title: String,
    val message: String,
    val channelId: String = "default_channel",
    val channelName: String = "General Notifications",
    val iconResId: Int? = null,
    val priority: Int = NotificationCompat.PRIORITY_DEFAULT,
    val importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
    val destinationUri: Uri,
)

