package com.reyaz.milliaconnect.ui.screen

data class UiStateLogin(
    var username: String = "",
    var password: String = "",
    var baseUrl: String = "",
    var message:String? = null
) {
    val loginEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
}