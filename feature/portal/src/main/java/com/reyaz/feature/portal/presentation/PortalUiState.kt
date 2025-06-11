package com.reyaz.feature.portal.presentation

data class PortalUiState(
    val username: String = "",
    val password: String = "",
    var message: String = "",
    val loadingMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val showNoWifiDialog: Boolean = false,
    val autoConnect: Boolean = true
){
    val loginEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
}