package com.reyaz.milliaconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.reyaz.milliaconnect.ui.navigation.AppNavHost
import com.reyaz.milliaconnect.ui.theme.WifiAutoConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WifiAutoConnectTheme {
                AppNavHost()
            }
        }
    }
}

