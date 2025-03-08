package com.reyaz.milliaconnect1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.reyaz.milliaconnect1.ui.navigation.AppNavHost
import com.reyaz.milliaconnect1.ui.theme.WifiAutoConnectTheme

class MainActivity : ComponentActivity() {
    // Register the permission request launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, you can now send notifications
                showToast("Notification permission granted!")
            } else {
                // Permission denied, handle accordingly
                showToast("Notification permission denied!")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestNotificationPermission()
        enableEdgeToEdge()
        setContent {
            WifiAutoConnectTheme {
                AppNavHost()
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        // Check if the device is running Android 13 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the permission is already granted
            val permissionStatus = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted
//                showToast("Notification permission already granted!")
            } else {
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // For devices below Android 13, no need to request permission
            showToast("Notification permission not required for this Android version.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}