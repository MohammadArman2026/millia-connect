package com.reyaz.milliaconnect.ui.screen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun WifiLoginApp(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    if(isConnectedToUniversityWifi(context)) {
        Log.d("WifiLoginApp", "Connected to University Wifi")
        WebViewScreen(modifier = modifier)
    } else{
        Log.d("WifiLoginApp", "Not Connected to University Wifi")
        Text("Not Connected to University Wifi", modifier = modifier.padding(16.dp))
    }

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