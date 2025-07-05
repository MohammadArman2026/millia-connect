package com.reyaz.feature.portal.data.repository

import com.reyaz.core.common.utils.Resource
import com.reyaz.feature.portal.data.PortalScraper
import com.reyaz.feature.portal.data.local.PortalDataStore
import com.reyaz.feature.portal.domain.model.ConnectRequest
import com.reyaz.feature.portal.domain.repository.PortalRepository
import kotlinx.coroutines.flow.Flow

class PortalRepositoryImpl(
    private val dataStore: PortalDataStore,
    private val portalScraper: PortalScraper
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

    override suspend fun connect(request: ConnectRequest): Flow<Resource<String>> =
        portalScraper.performLogin(username = request.username, password = request.password)


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
        val isJmiWifi = portalScraper.isJmiWifi()
        val hasInternetAccess = portalScraper.isJmiWifi(forceUseWifi = false)
        return if (isJmiWifi && hasInternetAccess) {
            JmiWifiState.LOGGED_IN
        } else if (isJmiWifi) {
            JmiWifiState.NOT_LOGGED_IN
        } else {
            JmiWifiState.NOT_CONNECTED
        }
    }

    @Deprecated("Already checking while logging in in portal scraper")
    override suspend fun isWifiPrimary(): Boolean {
        val res = portalScraper.isJmiWifi(false)
        // Log.d(TAG, "isWifiPrimary: $res")
        return res
    }
}

private const val TAG = "PORTAL_REPO_IMPL"

enum class JmiWifiState(val error: String? = null) {
    NOT_CONNECTED("You're Not Connected with JMI-WiFi"),
    NOT_LOGGED_IN,
    LOGGED_IN,
}