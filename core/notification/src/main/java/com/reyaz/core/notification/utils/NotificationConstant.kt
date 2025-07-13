package com.reyaz.core.notification.utils

import android.app.NotificationManager

enum class NotificationConstant(
    val channelName: String,
    val channelId: String,
    val importance: Int
) {
    RESULT_CHANNEL(
        channelName = "Entrance Result",
        channelId = "result_channel",
        importance = NotificationManager.IMPORTANCE_HIGH
    ),
    PORTAL_CHANNEL(
        channelName = "Portal Notification",
        channelId = "portal_channel",
        importance = NotificationManager.IMPORTANCE_LOW
    )
}

/*
NotificationManager.IMPORTANCE_NONE        // No sound or visual interruption
NotificationManager.IMPORTANCE_MIN         // No sound, not in status bar
NotificationManager.IMPORTANCE_LOW         // No sound, no heads up
NotificationManager.IMPORTANCE_DEFAULT     // Makes sound, shows in UI
NotificationManager.IMPORTANCE_HIGH        // Makes sound, heads-up
*/