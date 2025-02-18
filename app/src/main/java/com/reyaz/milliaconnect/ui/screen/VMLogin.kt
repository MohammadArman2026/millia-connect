package com.reyaz.milliaconnect.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.milliaconnect.data.UserPreferences
import com.reyaz.milliaconnect.data.WebLoginManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VMLogin(
    private val userPreferences: UserPreferences,
    private val webLoginManager: WebLoginManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateLogin())
    val uiState: StateFlow<UiStateLogin> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            handleLogin("202207696", "ique@7696595")
            _uiState.update {
                it.copy(
                    username = userPreferences.username.first(),
                    password = userPreferences.password.first(),
                    baseUrl = userPreferences.baseUrl.first()
                )
            }
        }
    }

    fun updateUsername(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun updateBaseUrl(newBaseUrl: String) {
        _uiState.update { it.copy(baseUrl = newBaseUrl) }
    }

    fun updateMessage(newMessage: String?) {
        _uiState.update { it.copy(message = newMessage) }
    }

    fun saveCredentials() {
        viewModelScope.launch {
            userPreferences.saveCredentials(
                _uiState.value.username,
                _uiState.value.password,
                _uiState.value.baseUrl
            )
        }
    }

    fun handleLogin(username: String, password: String) {
        viewModelScope.launch {
            Log.d("VMLogin", "Handling login for username: $username")
            webLoginManager.performLogin(username, password)
                .onSuccess { message ->
                    Log.d("VMLogin", "Login successful")
                }
                .onFailure { exception ->
//                    onLoginError("Network error: ${exception.message}")
                    Log.e("VMLogin", "Login failed", exception)
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            webLoginManager.performLogout()
        }
    }
}

