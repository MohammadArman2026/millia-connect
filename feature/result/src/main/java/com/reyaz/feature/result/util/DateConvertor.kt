package com.reyaz.feature.result.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun String.dateStringToLong(format: String = "dd-MM-yy"): Long? {
    return try {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.parse(this)?.time
    } catch (e: Exception) {
        null
    }
}

fun Long.longToDateString(format: String = "d MMM yyyy"): String? {
    return try {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.format(Date(this))
    } catch (e: Exception) {
        null
    }
}

fun Long.toTimeAgoString(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours   = minutes / 60
    val days    = hours / 24
    val weeks   = days / 7

    return when {
        seconds < 60        -> "Just now"
        minutes < 60        -> "$minutes min ago"
        hours < 24          -> "$hours hour${if (hours > 1) "s" else ""} ago"
        days < 7            -> "$days day${if (days > 1) "s" else ""} ago"
        weeks < 5           -> "$weeks week${if (weeks > 1) "s" else ""} ago"
        else                -> SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date(this))
    }
}
