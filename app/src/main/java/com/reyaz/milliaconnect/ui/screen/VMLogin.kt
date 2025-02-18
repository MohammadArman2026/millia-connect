package com.reyaz.milliaconnect.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
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
    private val webLoginManager: WebLoginManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateLogin())
    val uiState: StateFlow<UiStateLogin> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    username = userPreferences.username.first(),
                    password = userPreferences.password.first(),
                    isLoggedIn = userPreferences.loginStatus.first()
                )
            }
            handleLogin()
        }
    }

    fun updateUsername(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun updateMessage(newMessage: String?) {
        _uiState.update { it.copy(message = newMessage) }
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
}

