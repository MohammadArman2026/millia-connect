package com.reyaz.feature.portal.presentation


data class PortalUiState(
    val username: String = "",
    val password: String = "",
    var message: String? = null,
    val loadingMessage: String? = "Loading...",
    val isLoggedIn: Boolean = false,
    val isJamiaWifi: Boolean = true,
    val autoConnect: Boolean = true,
    val isMobileDataOn: Boolean = false,
    val primaryErrorMsg: String? = null
){
    val loginEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
    val isLoading: Boolean  = !loadingMessage.isNullOrBlank()
}