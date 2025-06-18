package com.reyaz.milliaconnect1.ui.screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.milliaconnect1.data.UserPreferences
import com.reyaz.milliaconnect1.data.WebLoginManager
import com.reyaz.milliaconnect1.util.NetworkConnectivityObserver
import com.reyaz.milliaconnect1.util.NetworkPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VMLogin(
    private val userPreferences: UserPreferences,
    private val webLoginManager: WebLoginManager,
    private val networkObserver: NetworkConnectivityObserver,
    private val appContext: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateLogin())
    val uiState: StateFlow<UiStateLogin> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Observe WiFi connectivity
            /*networkObserver.observeWifiConnectivity().collect { isWifiConnected ->
                _uiState.update { it.copy(showNoWifiDialog = !isWifiConnected) }
                if (isWifiConnected) {
                    _uiState.update {
                        it.copy(
                            username = userPreferences.username.first(),
                            password = userPreferences.password.first(),
                            isLoggedIn = userPreferences.loginStatus.first(),
                            autoConnect = userPreferences.autoConnect.first()
                        )
                    }
                    if (uiState.value.loginEnabled) handleLogin()
                    else _uiState.update { it.copy(errorMessage = "You only need to enter your credentials once.") }
                }
            }*/
            networkObserver.observeNetworkPreference().collect() { preference ->
                when (preference) {
                    NetworkPreference.BOTH_CONNECTED -> {
                        Log.d("Network", "Both connected")
                        // Show alert to user suggesting they turn off mobile data
                        _uiState.update { it.copy(isMobileDataOn = true, showNoWifiDialog = false) }
                        // login start
                        initialize()
                    }

                    NetworkPreference.WIFI_ONLY -> {
                        _uiState.update { it.copy(isMobileDataOn = false, showNoWifiDialog = false) }
                        // Ideal state - WiFi only
                        Log.d("Network", "WiFi connection only - optimal!")
                        // login start
                        initialize()
                    }

                    NetworkPreference.MOBILE_DATA_ONLY -> {
                        // May want to show different UI or behavior
                        Log.d("Network", "Mobile data only")
                    }

                    NetworkPreference.NONE -> {
                        // No connectivity
                        _uiState.update { it.copy(showNoWifiDialog = true) }
                    }
                }
            }
        }
    }

    private fun initialize() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    username = userPreferences.username.first(),
                    password = userPreferences.password.first(),
                    isLoggedIn = userPreferences.loginStatus.first(),
                    autoConnect = userPreferences.autoConnect.first()
                )
            }
            if (uiState.value.loginEnabled) handleLogin()
            else _uiState.update { it.copy(errorMessage = "You only need to enter your credentials once.") }
        }
    }

    fun handleLogin() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingMessage = "Connecting...") }
            webLoginManager.performLogin(_uiState.value.username, _uiState.value.password)
                .onSuccess {
//                    Log.d("VMLogin", "Login successful")
                    _uiState.update {
                        it.copy(
                            errorMessage = null,
                            isLoggedIn = true,
                            loadingMessage = null
                        )
                    }
                    saveCredentials(true)
                    //if (_uiState.value.autoConnect) AutoLoginWorker.schedule(context = appContext)
                }
                .onFailure { exception ->
                   // AutoLoginWorker.cancel(appContext)
                    onError(exception)
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingMessage = "Disconnecting...") }
            webLoginManager.performLogout()
                .onSuccess { message ->
                    Log.d("VMLogin", "Logout successful")
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            loadingMessage = null,
                            errorMessage = null
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
            else /*exception.message ?: */ "Oops! An error occurred."

            _uiState.update {
                it.copy(
                    loadingMessage = null,
                    errorMessage = error
                )
            }

        }
    }

    fun dismissNoWifiDialog() {
        _uiState.update { it.copy(showNoWifiDialog = false) }
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
                //AutoLoginWorker.cancel(context = context)
            else if (uiState.value.isLoggedIn && uiState.value.loginEnabled) {
                //AutoLoginWorker.schedule(context = context)
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
}

