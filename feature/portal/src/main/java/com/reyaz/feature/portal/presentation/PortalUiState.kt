package com.reyaz.feature.portal.presentation

data class PortalUiState(
    val username: String = "202207696",
    val password: String = "ique@7696595",
    var message: String? = "null",
    val loadingMessage: String? = "Loading...",
    val isLoggedIn: Boolean = false,
    val isJamiaWifi: Boolean = true,
    val autoConnect: Boolean = true
){
    val loginEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
    val isLoading: Boolean  = !loadingMessage.isNullOrBlank()
}