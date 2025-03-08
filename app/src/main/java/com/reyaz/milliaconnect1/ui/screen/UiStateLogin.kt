package com.reyaz.milliaconnect1.ui.screen

data class UiStateLogin(
    val username: String = "",
    val password: String = "",
    var errorMessage: String? =null,
    val loadingMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val isWifiConnected: Boolean = true,
    val autoConnect: Boolean = true
){
    val loginEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
    val showDialog: Boolean = isWifiConnected
}