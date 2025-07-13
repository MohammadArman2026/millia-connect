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

/**
Defined when creating a Notification Channel. You create it once per channel.


NotificationManager.IMPORTANCE_NONE        // No sound or visual interruption
NotificationManager.IMPORTANCE_MIN         // No sound, not in status bar
NotificationManager.IMPORTANCE_LOW         // No sound
NotificationManager.IMPORTANCE_DEFAULT     // Makes sound, shows in UI
NotificationManager.IMPORTANCE_HIGH        // Makes sound, heads-up

val channel = NotificationChannel(
    "my_channel_id",
    "General Notifications",
    NotificationManager.IMPORTANCE_HIGH
)

Used directly in the notification builder to control importance at runtime (no channels).
f you set PRIORITY_HIGH but the channel has IMPORTANCE_LOW, the notification won’t show heads-up or sound. The channel setting always wins on API 26+.

| Priority Constant  | Value | Description                                                                      |
| ------------------ | ----- | -------------------------------------------------------------------------------- |
| `PRIORITY_MIN`     | -2    | Silent and hidden in the status bar.                                             |
| `PRIORITY_LOW`     | -1    | Silent, shows in the shade (notification drawer) but **no heads-up**.            |
| `PRIORITY_DEFAULT` | 0     | Standard priority. Plays sound if set, shows icon, no heads-up by default.       |
| `PRIORITY_HIGH`    | 1     | Interruptive. Plays sound and shows as a **heads-up** notification (if allowed). |
| `PRIORITY_MAX`     | 2     | Most intrusive. Used for urgent, full-screen notifications like alarms or calls. |


val builder = NotificationCompat.Builder(context, "my_channel_id")
    .setPriority(NotificationCompat.PRIORITY_HIGH)

*/
