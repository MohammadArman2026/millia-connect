package com.reyaz.core.notification.utils

enum class NotificationConstant(val channelName: String, val channelId: String){
    RESULT_CHANNEL("Entrance Result", "result_channel"),
    PORTAL_CHANNEL(channelName = "Portal Notification", channelId = "portal_channel")
}