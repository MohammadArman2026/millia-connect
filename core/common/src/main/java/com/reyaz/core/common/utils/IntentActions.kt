package com.reyaz.core.common.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast

// share app link
fun Context.shareTextExternally(
    mainText: String,
    subText: String? = null,
    title: String = "Share Millia Connect via"
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subText)
        putExtra(Intent.EXTRA_TEXT, mainText)
    }
    startActivity(Intent.createChooser(intent, title))
}

private const val TAG = "NAVIGATION_ACTION"
fun Context.openUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        this.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "No app found to open url", Toast.LENGTH_SHORT).show()
        Log.e(TAG, "No activity found to handle intent", e)
    } catch (e: Exception) {
        Toast.makeText(this, "Failed to open the link.", Toast.LENGTH_SHORT).show()
        Log.e(TAG, "Unexpected error opening URL: $url", e)
    }
}

fun Context.sendWhatsAppMessage(phoneNumber: String = "919518812358", message: String) {

    val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)

    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        Toast.makeText(this, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
    }
}

fun Context.shareNoticeExternally(title: String, link: String) {
    val shareText = buildString {
        append("📢 Notice: ")
        append(title)
        append("\n\n")
        append("🔗 Link: ")
        append(link)
        append("\n\n")
        append("Shared from *Millia Connect*.\n Download Now: https://play.google.com/store/apps/details?id=com.reyaz.milliaconnect1")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Notice: $title")
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    try {
        val chooser = Intent.createChooser(intent, "Share Notice via")
        startActivity(chooser)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "No app found to share notice", Toast.LENGTH_SHORT).show()
        Log.e("SHARE_NOTICE", "No app found to handle share intent", e)
    } catch (e: Exception) {
        Toast.makeText(this, "Failed to share the notice.", Toast.LENGTH_SHORT).show()
        Log.e("SHARE_NOTICE", "Unexpected error while sharing", e)
    }
}
