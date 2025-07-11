package com.reyaz.feature.portal.data.repository

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.reyaz.core.common.utils.Resource
import com.reyaz.core.notification.manager.AppNotificationManager
import com.reyaz.core.notification.model.NotificationData
import com.reyaz.core.notification.utils.NotificationConstant
import com.reyaz.feature.portal.data.local.PortalDataStore
import com.reyaz.feature.portal.data.remote.PortalScraper
import com.reyaz.feature.portal.data.worker.AutoLoginWorker
import com.reyaz.feature.portal.domain.model.ConnectRequest
import com.reyaz.feature.portal.domain.repository.PortalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PortalRepositoryImpl(
    private val dataStore: PortalDataStore,
    private val context: Context,
    private val portalScraper: PortalScraper,
    private val notificationManager: AppNotificationManager,
) : PortalRepository {

    override suspend fun saveCredential(request: ConnectRequest): Result<Unit> {
        return try {
            val result =
                dataStore.saveCredentials(
                    username = request.username,
                    password = request.password,
                    autoConnect = request.autoConnect
                )
            if (result.isSuccess) {
                Result.success(Unit)
            } else {
                // return Result.failure(result.exceptionOrNull() ?: Exception("Unknown Error"))
                throw Exception(result.exceptionOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun connect(shouldNotify: Boolean): Flow<Resource<String>> = flow {
        val username = dataStore.username.first()
        val password = dataStore.password.first()
        if (username != null && password != null) {
            portalScraper.performLogin(
                username = username,
                password = password
            ).collect {
                emit(it)
                when(it){
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        if (shouldNotify) {
                            showPortalNotification(title = "Automatically Logged In", message = "You're Successfully Logged in!")
                        }
                        if(dataStore.autoConnect.first())
                            AutoLoginWorker.scheduleOneTime(context)
                    }
                    is Resource.Loading -> {}
                }
            }
        } else {
            Log.d(TAG, "Invalid Credentials")
            if (shouldNotify)
                showPortalNotification(title = "Invalid Credentials", message = "Please open the app again and enter the valid credentials.")
            emit(Resource.Error("Invalid Credentials"))
        }
    }.flowOn(Dispatchers.IO)

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showPortalNotification(title: String, message: String) {
        notificationManager.showNotification(
            NotificationData(
                id = NotificationConstant.PORTAL_CHANNEL.hashCode(),
                title = title,
                message = message,
                channelId = NotificationConstant.PORTAL_CHANNEL.channelId,
                channelName = NotificationConstant.PORTAL_CHANNEL.channelName,
                priority = NotificationCompat.PRIORITY_LOW,
                importance = NotificationManager.IMPORTANCE_LOW
            )
        )
    }

    override suspend fun disconnect(): Result<String> {
        return try {
            val result =
                portalScraper.performLogout()
            if (result.isSuccess) {
                result
            } else {
                // return Result.failure(result.exceptionOrNull() ?: Exception("Unknown Error"))
                throw Exception(result.exceptionOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Checks the current connection state of the JMI Wi-Fi.
     *
     * This function determines whether the device is connected to the JMI Wi-Fi network
     * and if it has internet access. Based on these two conditions, it returns the
     * appropriate [JmiWifiState].
     *
     * @return [JmiWifiState] indicating the current connection status:
     *   - [JmiWifiState.LOGGED_IN] if connected to JMI Wi-Fi and has internet access.
     *   - [JmiWifiState.NOT_LOGGED_IN] if connected to JMI Wi-Fi but does not have internet access.
     *   - [JmiWifiState.NOT_CONNECTED] if not connected to JMI Wi-Fi.
     */
    override suspend fun checkConnectionState(): JmiWifiState {
        val isJmiWifi = portalScraper.isJmiWifi(forceWifi = true)
        val hasInternetAccess =
            portalScraper.isInternetAvailable(isCheckingForWifi = true).getOrNull() ?: false
        Log.d(TAG, "HasInternet: $hasInternetAccess, IsJmiWifi: $isJmiWifi")
        return if (isJmiWifi && hasInternetAccess) {
            JmiWifiState.LOGGED_IN
        } else if (isJmiWifi) {
            JmiWifiState.NOT_LOGGED_IN
        } else {
            JmiWifiState.NOT_CONNECTED
        }
    }
}

private const val TAG = "PORTAL_REPO_IMPL"

enum class JmiWifiState(val error: String? = null) {
    NOT_CONNECTED("You're Not Connected with JMI-WiFi"),
    NOT_LOGGED_IN,
    LOGGED_IN,
}