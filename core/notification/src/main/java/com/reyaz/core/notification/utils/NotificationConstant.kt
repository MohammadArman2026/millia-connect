package com.reyaz.core.notification.utils

import android.app.NotificationManager

enum class NotificationConstant(val channelName: String, val channelId: String, val importance: Int = NotificationManager.IMPORTANCE_DEFAULT){
    RESULT_CHANNEL("Entrance Result", "result_channel"),
    PORTAL_CHANNEL(channelName = "Portal Notification", channelId = "portal_channel", importance = NotificationManager.IMPORTANCE_LOW)
}

/*
NotificationManager.IMPORTANCE_NONE        // No sound or visual interruption
NotificationManager.IMPORTANCE_MIN         // No sound, not in status bar
NotificationManager.IMPORTANCE_LOW         // No sound
NotificationManager.IMPORTANCE_DEFAULT     // Makes sound, shows in UI
NotificationManager.IMPORTANCE_HIGH        // Makes sound, heads-up
*/