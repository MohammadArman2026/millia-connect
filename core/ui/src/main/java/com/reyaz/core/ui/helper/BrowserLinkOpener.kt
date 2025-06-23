package com.reyaz.core.ui.helper

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

private const val TAG = "LINK_HANDLER"

class LinkHandler(private val context: Context) {
    fun openInBrowser(link: String) {
        try {
            //Log.d(TAG, "Opening link in browser")
            if (link.isEmpty())
                throw Exception("Link is null or empty")
            val intent = Intent(Intent.ACTION_VIEW, link.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.d(TAG, "openInBrowser: $e")
        }
    }
}