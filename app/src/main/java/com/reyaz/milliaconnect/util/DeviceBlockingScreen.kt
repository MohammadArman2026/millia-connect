/*
package com.reyaz.milliaconnect.util

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.UUID
import android.net.wifi.WifiManager
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import java.security.MessageDigest

@Composable
fun DeviceBlockingScreen(
    blockedDeviceIds: List<String>,
    onDeviceBlocked: () -> Unit,
    onDeviceAllowed: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var isBlocked by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        val deviceId = getDeviceIdentifier(context)
        isBlocked = blockedDeviceIds.contains(deviceId)
        
        if (isBlocked) {
            onDeviceBlocked()
        } else {
            onDeviceAllowed()
        }
    }
    
    if (!isBlocked) {
        content()
    }
}
fun getAndroidId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
}

fun getDeviceIdentifier(context: Context): String {
    val androidId = getAndroidId(context)
    return hashString(androidId)
}

// Hash function to create consistent identifier
fun hashString(input: String): String {
    try {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    } catch (e: Exception) {
        return UUID.nameUUIDFromBytes(input.toByteArray()).toString()
    }
}

// Example usage in a ViewModel
class DeviceBlockingViewModel {
    private val blockedDevices = listOf(
        "af3e22b1d5c4b1e6", // Android ID of blocked device
        "6c89f1b4a3d2e7c9", // Another blocked device ID
        // Add more device IDs to block
    )
    
    fun isDeviceBlocked(deviceId: String): Boolean {
        return blockedDevices.contains(deviceId)
    }
    
    fun addBlockedDevice(deviceId: String) {
        // In a real app, you would store this to persistent storage
        // blockedDevices.add(deviceId)
    }
}

// Example implementation with Jetpack Compose
@Composable
fun AppEntryPoint() {
    val context = LocalContext.current
    val deviceId = remember { getDeviceIdentifier(context) }
    val viewModel = remember { DeviceBlockingViewModel() }
    
    DeviceBlockingScreen(
        blockedDeviceIds = viewModel.blockedDevices,
        onDeviceBlocked = {
            // Navigate to blocked screen or show message
        },
        onDeviceAllowed = {
            // Continue with normal app flow
        }
    ) {
        // Your app content here
        MainAppContent()
    }
}

@Composable
fun MainAppContent() {
    // Your main app UI
}*/
