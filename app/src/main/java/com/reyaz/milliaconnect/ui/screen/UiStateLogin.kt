package com.reyaz.milliaconnect.ui.screen

data class UiStateLogin(
    val username: String = "",
    val password: String = "",
    val message: String = "",
    val loadingMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val showNoWifiDialog: Boolean = false,
    val autoConnect: Boolean = false
){
    val loginEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
}