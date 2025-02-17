package com.reyaz.wifiautoconnect.ui.screen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException

@Composable
fun WifiLoginApp(
    modifier: Modifier = Modifier
) {
    WebViewScreen(modifier = modifier)
    /*var status by remember { mutableStateOf("Checking Wi-Fi...") }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        status = checkWifiAndLogin(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = status)
    }
}

private suspend fun checkWifiAndLogin(context: Context): String {
    return withContext(Dispatchers.IO) {
        if (isConnectedToUniversityWifi(context)) {
            if (loginToCaptivePortal("202207696", "ique@7696595")) {
                "Logged in to University Wi-Fi!"
            } else {
                "Failed to log in."
            }
        } else {
            "Not connected to University Wi-Fi."
        }
    }*/
}

private fun isConnectedToUniversityWifi(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null &&
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

private suspend fun loginToCaptivePortal(username: String, password: String): Boolean {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
            .add("4Tredir", "http://10.2.0.10:8090/login?03419531c89cc9f0")
            .add("magic", "01419e35ce90cde5")
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("http://10.2.0.10:8090/")
            .post(formBody)
            .build()
        Log.d("RESPONSE", "Response code: $request")

        try {
            val response = client.newCall(request).execute()
            Log.d("RESPONSE", "Response code: ${response.code}")
            response.isSuccessful
        } catch (e: IOException) {
            false
        }
    }
}