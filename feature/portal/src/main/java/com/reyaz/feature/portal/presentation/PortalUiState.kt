package com.reyaz.feature.portal.presentation


data class PortalUiState(
    val username: String = "",
    val password: String = "",

    val isLoggedIn: Boolean = false,
    val isJamiaWifi: Boolean = true,
    val autoConnect: Boolean = true,
    val isWifiPrimary: Boolean = true,

    val loadingMessage: String? = "Loading...",
    val errorMsg: String? = null,
){
    val loginBtnEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
    val isLoading: Boolean  = !loadingMessage.isNullOrBlank()
}

// not connected to preferred wifi
// connected but not logged in
// logged in
// loading