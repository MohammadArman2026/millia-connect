package com.reyaz.core.common.utils

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
        seconds < 60        -> "Fetched Just now"
        minutes < 60        -> "Fetched $minutes min ago"
        hours < 24          -> "Fetched $hours hour${if (hours > 1) "s" else ""} ago"
        days < 7            -> "Fetched $days day${if (days > 1) "s" else ""} ago"
        weeks < 5           -> "Fetched $weeks week${if (weeks > 1) "s" else ""} ago"
        else                -> "Fetched on ${SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date(this))}"
    }
}
