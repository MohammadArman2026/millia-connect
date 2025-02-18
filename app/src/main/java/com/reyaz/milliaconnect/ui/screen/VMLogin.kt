package com.reyaz.milliaconnect.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.milliaconnect.data.UserPreferences
import com.reyaz.milliaconnect.data.WebLoginManager
import com.reyaz.milliaconnect.util.NetworkConnectivityObserver
import com.reyaz.milliaconnect.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VMLogin(
    private val userPreferences: UserPreferences,
    private val webLoginManager: WebLoginManager,
    private val notificationHelper: NotificationHelper,
    private val networkObserver: NetworkConnectivityObserver // Add this
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateLogin())
    val uiState: StateFlow<UiStateLogin> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Observe WiFi connectivity
            networkObserver.observeWifiConnectivity()
                .collect { isWifiConnected ->
                    _uiState.update {
                        it.copy(
                            showNoWifiDialog = !isWifiConnected,
                            message = if (!isWifiConnected) "Please connect to WiFi" else null
                        )
                    }
                    if (isWifiConnected) {
                       // handleLogin()
                    }
                }
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    username = userPreferences.username.first(),
                    password = userPreferences.password.first(),
                    isLoggedIn = userPreferences.loginStatus.first()
                )
            }
            Log.d("VMLogin", "init: ${_uiState.value.username}")
//            handleLogin()
        }
    }

    fun dismissNoWifiDialog() {
        _uiState.update { it.copy(showNoWifiDialog = false) }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    private fun saveCredentials(isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreferences.saveCredentials(
                _uiState.value.username,
                _uiState.value.password,
                isLoggedIn
            )
        }
    }

    fun handleLogin() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingMessage = "Logging in...") }
            webLoginManager.performLogin(_uiState.value.username, _uiState.value.password)
                .onSuccess { message ->
//                    Log.d("VMLogin", "Login successful")
                    _uiState.update {
                        it.copy(
                            message = message,
                            isLoggedIn = true,
                            loadingMessage = null
                        )
                    }
                    //notificationHelper.showNotification("Auto Login", "Logged in Successfully")   TODO: notification
                    saveCredentials(true)
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(message = exception.message, loadingMessage = null) }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingMessage = "Logging Out...") }
            webLoginManager.performLogout()
                .onSuccess { message ->
                    Log.d("VMLogin", "Logout successful")
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            loadingMessage = null,
                            message = message
                        )
                    }
                    saveCredentials(false)
                }
                .onFailure { exception ->
                    Log.e("VMLogin", "Logout failed", exception)
                    _uiState.update { it.copy(loadingMessage = null, message = exception.message) }
                }
        }
    }

    fun updateAutoConnect(autoConnect: Boolean) {
        _uiState.update { it.copy(autoConnect = autoConnect) }
    }
}

