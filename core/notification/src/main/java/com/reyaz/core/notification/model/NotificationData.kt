package com.reyaz.core.notification.model

import androidx.core.app.NotificationCompat

data class NotificationData(
    val id: Int = 0,
    val title: String,
    val message: String,
    val channelId: String = "default_channel",
    val channelName: String = "General Notifications",
    val iconResId: Int? = null,
    val priority: Int = NotificationCompat.PRIORITY_DEFAULT
)
