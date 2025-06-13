package com.reyaz.core.common.utlis


import android.content.Context
import android.content.Intent
import android.net.CaptivePortal
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.combine

class NetworkManager(private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Store the CaptivePortal instance when available
    private var captivePortal: CaptivePortal? = null

    // Method to set the captive portal from the activity receiving ACTION_CAPTIVE_PORTAL_SIGN_IN
    fun setCaptivePortal(intent: Intent?) {
        captivePortal = intent?.getParcelableExtra(ConnectivityManager.EXTRA_CAPTIVE_PORTAL)
        Log.d("NetworkConnectivityObserver", "CaptivePortal object received: ${captivePortal != null}")
    }

    // Method to report captive portal dismissed
    fun reportCaptivePortalDismissed() {
        captivePortal?.let {
            Log.d("NetworkConnectivityObserver", "Reporting captive portal dismissed")
            it.reportCaptivePortalDismissed()
            captivePortal = null
        }
    }

    fun forceUseWifi() {
        Log.d("WifiNetworkManager", "Forcing Wi-Fi usage...")
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.requestNetwork(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                connectivityManager.bindProcessToNetwork(network) // Force Wi-Fi usage
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                connectivityManager.bindProcessToNetwork(null) // Reset to default
            }
        })
    }

    fun observeWifiConnectivity(): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                val isWifi = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
                trySend(isWifi)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val isWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                trySend(isWifi)
            }
        }

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Initial state
        val isWifiConnected = isWifiCurrentlyConnected()
        trySend(isWifiConnected)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    private fun observeMobileDataConnectivity(): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                val isMobileData = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
                trySend(isMobileData)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val isMobileData = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                trySend(isMobileData)
            }
        }

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Initial state
        val isMobileDataConnected = isMobileDataCurrentlyConnected()
        trySend(isMobileDataConnected)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    // Combine both WiFi and Mobile data statuses into a single flow
    fun observeNetworkPreference(): Flow<NetworkPreference> = combine(
        observeWifiConnectivity(),
        observeMobileDataConnectivity()
    ) { isWifiConnected, isMobileDataConnected ->
        when {
            isWifiConnected && isMobileDataConnected -> NetworkPreference.BOTH_CONNECTED
            isWifiConnected && !isMobileDataConnected -> NetworkPreference.WIFI_ONLY
            //isMobileDataConnected -> NetworkPreference.MOBILE_DATA_ONLY
            else -> NetworkPreference.NONE
        }
    }

    private fun isWifiCurrentlyConnected(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }

    private fun isMobileDataCurrentlyConnected(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
    }

}

// Enum to represent the network preference status
enum class NetworkPreference {
    WIFI_ONLY,
    // MOBILE_DATA_ONLY,
    BOTH_CONNECTED,
    NONE
}