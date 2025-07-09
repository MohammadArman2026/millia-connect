package com.reyaz.core.common.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.CaptivePortal
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

private const val TAG = "NETWORK_MANAGER"

class NetworkManager(private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var captivePortal: CaptivePortal? = null
    private var wifiForceCallback: ConnectivityManager.NetworkCallback? = null

    private val enableLogging = false
    private fun log(message: String) {
        if (enableLogging) Log.d(TAG, message)
    }

    fun setCaptivePortal(intent: Intent?) {
        captivePortal = intent?.getParcelableExtra(ConnectivityManager.EXTRA_CAPTIVE_PORTAL)
        log("CaptivePortal object received: ${captivePortal != null}")
    }

    fun reportCaptivePortalDismissed() {
        captivePortal?.let {
            log("Reporting captive portal dismissed")
            it.reportCaptivePortalDismissed()
            captivePortal = null
        }
    }

    /**
     * Binds the current process to a Wi-Fi network.
     *
     * This function requests a Wi-Fi network with internet capability.
     * When a suitable Wi-Fi network becomes available, the process is bound to it.
     * If the Wi-Fi network is lost, the process binding is reset.
     *
     * It also ensures that any previously registered network callback for forcing Wi-Fi
     * is unregistered before registering a new one.
     */
    fun bindToWifiNetwork() {
        log("Binding to Wi-Fi network...")
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectivityManager.bindProcessToNetwork(network)
                log("Wi-Fi network bound")
            }

            override fun onLost(network: Network) {
                connectivityManager.bindProcessToNetwork(null)
                log("Wi-Fi network lost, reset binding")
            }
        }

        // Cleanup previous callback if any
        wifiForceCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }

        wifiForceCallback = callback
        connectivityManager.requestNetwork(request, callback)
    }

    /**
     * Resets the network binding to the default (null).
     * This function unbinds the process from any specific network and
     * unregisters the `wifiForceCallback` if it was previously set.
     */
    fun resetNetworkBinding() {
        log("Resetting network binding")
        connectivityManager.bindProcessToNetwork(null)
        wifiForceCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
            wifiForceCallback = null
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun observeWifiConnectivity(): Flow<Boolean> =
        observeConnectivity(NetworkCapabilities.TRANSPORT_WIFI)

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
     fun observeMobileDataConnectivity(): Flow<Boolean> =
        observeConnectivity(NetworkCapabilities.TRANSPORT_CELLULAR)

    /**
     * Observes the connectivity status for a specific network transport type.
     *
     * This function uses a [callbackFlow] to emit `true` when the specified transport type is available
     * and `false` when it is lost or unavailable. It registers a [ConnectivityManager.NetworkCallback]
     * to listen for network changes and updates the flow accordingly.
     *
     * The flow also emits the initial connectivity state upon subscription.
     *
     * The [distinctUntilChanged] operator ensures that only distinct connectivity states are emitted.
     *
     * @param transportType The network transport type to observe (e.g., [NetworkCapabilities.TRANSPORT_WIFI], [NetworkCapabilities.TRANSPORT_CELLULAR]).
     * @return A [Flow] of [Boolean] indicating the connectivity status (true if connected, false otherwise).
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun observeConnectivity(transportType: Int): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
            override fun onAvailable(network: Network) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                val hasTransport = capabilities?.hasTransport(transportType) == true
//                trySend(hasTransport)
                trySend(true)
                log("onAvailable: $transportType = $hasTransport")
            }

            @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
            override fun onLost(network: Network) {
                // Only emit false if this was the last network of this type
                val stillConnected = getAllNetworksConnected(transportType)
//                trySend(stillConnected)
                trySend(false)
                log("onLost: $transportType, still connected: $stillConnected")
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
//                val hasTransport = capabilities.hasTransport(transportType)
//                trySend(hasTransport)
//                log("onCapabilitiesChanged: $transportType = $hasTransport")
            }
        }

        val request = NetworkRequest.Builder()
            .addTransportType(transportType)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Get initial state more comprehensively
        val connected = getAllNetworksConnected(transportType)
        trySend(connected)
        log("Initial state: $transportType = $connected")

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
            log("NetworkCallback unregistered: $transportType")
        }
    }.distinctUntilChanged()

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun observeAllNetworkType(): Flow<NetworkPreference> = combine(
        observeWifiConnectivity(),
        observeMobileDataConnectivity()
    ) { isWifiConnected, isMobileDataConnected ->
        when {
            isWifiConnected && isMobileDataConnected -> {
                log("Both wifi and mobile data connected")
                NetworkPreference.BOTH_CONNECTED
            }
            isWifiConnected -> {
                log("Only wifi connected")
                NetworkPreference.WIFI_ONLY
            }
            isMobileDataConnected -> {
                log("Only mobile data connected")
                NetworkPreference.MOBILE_DATA_ONLY
            }
            else -> {
                log("No connection")
                NetworkPreference.NONE
            }
        }
    }


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isCurrentlyConnected(transportType: Int): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(transportType)
    }

    /**
     * Checks if any network of the specified transport type is available,
     * not just the active network.
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun getAllNetworksConnected(transportType: Int): Boolean {
        return try {
            val allNetworks = connectivityManager.allNetworks
            allNetworks.any { network ->
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                capabilities?.hasTransport(transportType) == true
            }
        } catch (e: Exception) {
            log("Error checking all networks: ${e.message}")
            // Fallback to active network check
            isCurrentlyConnected(transportType)
        }
    }
}

enum class NetworkPreference {
    WIFI_ONLY,
    MOBILE_DATA_ONLY,
    BOTH_CONNECTED,
    NONE
}
