package com.reyaz.milliaconnect1

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.WorkManager
import com.reyaz.core.ui.theme.MilliaConnectTheme
import com.reyaz.milliaconnect1.util.NetworkConnectivityObserver

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
        val splashScreen  = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{false}

        checkAndRequestNotificationPermission()
        // Check if this activity was launched from a captive portal notification
        if (intent?.action == ConnectivityManager.ACTION_CAPTIVE_PORTAL_SIGN_IN) {
            Log.d("MAIN_ACTIVITY", "Captive portal intent received")
            val networkConnectivityObserver = NetworkConnectivityObserver(this)
            networkConnectivityObserver.setCaptivePortal(intent)

            // You might want to automatically trigger login here or wait for user input
        } else
            Log.d("MAIN_ACTIVITY", "No Captive Portal Intent Received!")

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            MilliaConnectTheme {
                Surface {
                    MilliaConnectApp()
                }
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