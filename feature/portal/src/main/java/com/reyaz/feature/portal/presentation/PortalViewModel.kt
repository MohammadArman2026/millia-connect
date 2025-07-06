package com.reyaz.feature.portal.presentation


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.common.utils.Resource
import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.core.common.utils.NetworkPreference
import com.reyaz.feature.portal.data.local.PortalDataStore
import com.reyaz.feature.portal.data.repository.JmiWifiState
import com.reyaz.feature.portal.domain.model.ConnectRequest
import com.reyaz.feature.portal.domain.repository.PortalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "PORTAL_VM"
private const val LOGGING = true

class PortalViewModel(
    private val repository: PortalRepository,
    private val networkObserver: NetworkManager,
    private val userPreferences: PortalDataStore,
//    private val portalScraper: PortalScraper,
//    private val appContext: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PortalUiState(
//            username = "202207696",
//            password = "ique@7696595",
//            isJamiaWifi = true,
//            isLoggedIn = false,
//            autoConnect = false,
//            errorMsg = null,
//            loadingMessage = null

        )
    )
    val uiState: StateFlow<PortalUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingMessage = "Loading...") }
            fetchCredentials()
            networkObserver.observeNetworkType().collect { networkPreference ->
                when (networkPreference) {
                    NetworkPreference.BOTH_CONNECTED, NetworkPreference.WIFI_ONLY -> {
                        Log.d(TAG, "Both or only wifi connected")
                        // login start
                        initialize()
                    }

                    else -> {
                        Log.d(TAG, "No connection")
                        _uiState.update {
                            it.copy(
                                isJamiaWifi = false,
                                loadingMessage = null,
                                isLoggedIn = false,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun log(log: String)  { if (LOGGING) Log.d(TAG, log) }

    private suspend fun fetchCredentials() {
        _uiState.update {
            it.copy(
                username = userPreferences.username.first(),
                password = userPreferences.password.first(),
                autoConnect = userPreferences.autoConnect.first(),
            )
        }
    }


    fun handleLogin() {
        viewModelScope.launch {
            //repository.checkConnectionState()
//            if (uiState.value.loadingMessage == null) _uiState.update { it.copy(loadingMessage = "Logging in...") } // todo uncomment
            val request = ConnectRequest(
                _uiState.value.username,
                _uiState.value.password,
                _uiState.value.autoConnect
            )
            repository.connect(request).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                errorMsg = null,
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                errorMsg = resource.message,
                                isLoggedIn = true,
                                loadingMessage = null,
                                isWifiPrimary = resource.message.isNullOrBlank()
                            )
                        }
                        //updatePrimaryConnectionError()    // todo: uncomment
                        saveCredentials(true)
//                    if (_uiState.value.autoConnect)
                        //AutoLoginWorker.schedule(context = appContext)
                    }

                    is Resource.Error -> {
                        //AutoLoginWorker.cancel(appContext)
                        _uiState.update {
                            it.copy(
                                isLoggedIn = false,
                                loadingMessage = null,
                                errorMsg = resource.message
                            )
                        }
                        saveCredentials(false)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingMessage = "Logging Out...") }
            repository.disconnect()
                .onSuccess { message ->
                    Log.d("VMLogin", "Logout successful")
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            loadingMessage = null,
                            errorMsg = message
                        )
                    }
                    saveCredentials(false)
                }
                .onFailure { exception ->
                    Log.e("VMLogin", "Logout failed", exception)
                    onError(exception)
                }
            //AutoLoginWorker.cancel(appContext)
        }
    }

    private fun onError(exception: Throwable) {
        viewModelScope.launch {
            val error = if (exception.message?.contains("10.2.0.10:8090") == true)
                "You're not connected to Jamia Wifi.\nPlease connect and try again."
            else if (exception.message?.contains("Wrong Username or Password") == true)
                "Wrong Username or Password"
            else
                exception.message ?: "Oops! An error occurred."

            _uiState.update {
                it.copy(
                    loadingMessage = null,
                    errorMsg = error
                )
            }

        }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
        viewModelScope.launch {
            saveCredentials(isLoggedIn = false)
        }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
        viewModelScope.launch {
            saveCredentials(isLoggedIn = false)
        }
    }

    fun updateAutoConnect(autoConnect: Boolean, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(autoConnect = autoConnect) }
            if (!uiState.value.autoConnect)
//                AutoLoginWorker.cancel(context = context)
            else if (uiState.value.isLoggedIn && uiState.value.loginEnabled) {
//                AutoLoginWorker.schedule(context = context)
            }
            userPreferences.setAutoConnect(autoConnect)
        }
        viewModelScope.launch {
            saveCredentials(isLoggedIn = false)
        }
    }

    private fun saveCredentials(isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreferences.saveCredentials(
                _uiState.value.username,
                _uiState.value.password,
                isLoggedIn,
                _uiState.value.autoConnect
            )
        }
    }

    fun retry() {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
//            _uiState.update { it.copy(loadingMessage = "Connecting...") }
            when (repository.checkConnectionState()) {
                JmiWifiState.NOT_CONNECTED -> {
                    log("Not Connected")
                    _uiState.update {
                        it.copy(
                            loadingMessage = null,
                            isJamiaWifi = false,
                            isLoggedIn = false,
                            errorMsg = null
                        )
                    }
//                    saveCredentials(false)
                }

                JmiWifiState.NOT_LOGGED_IN -> {
                    log("Not logged in")
                    _uiState.update {
                        it.copy(
                            loadingMessage = null,
                            isLoggedIn = false,
                            isJamiaWifi = true,
                            errorMsg = if (uiState.value.loginEnabled) null else "One time credential needed to login automatically.",
                        )
                    }
                    if (uiState.value.loginEnabled) {
                        handleLogin()
//                        saveCredentials(true)
                    }
                }

                JmiWifiState.LOGGED_IN -> {
                    log("Logged in")
                    _uiState.update {
                        it.copy(
                            loadingMessage = null,
                            isLoggedIn = true,
                            isJamiaWifi = true,
                            errorMsg = null,
                        )
                    }
//                    updatePrimaryConnectionError()
//                    saveCredentials(true)
                }
            }
        }
    }

    private suspend fun updatePrimaryConnectionError() =
        _uiState.update {
            it.copy(errorMsg = if (repository.isCurrentConnectionIsJmiWifi()) null else "Turn Off Mobile Data to make wifi your primary connection.")
        }

}
