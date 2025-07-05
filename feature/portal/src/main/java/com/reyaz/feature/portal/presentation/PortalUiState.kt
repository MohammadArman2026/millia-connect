package com.reyaz.feature.portal.presentation


data class PortalUiState(
    val username: String = "",
    val password: String = "",

    val isLoggedIn: Boolean = false,
    val isJamiaWifi: Boolean = true,
    val autoConnect: Boolean = true,
//    val isMobileDataOn: Boolean = false,

    val loadingMessage: String? = "Loading...",
    val errorMsg: String? = null,
){
    val loginEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
    val isLoading: Boolean  = !loadingMessage.isNullOrBlank()
}