package com.reyaz.milliaconnect.ui.screen

data class UiStateLogin(
//    var isLoading:Boolean = true,
    var loadingMessage: String? = null,
    var username: String = "",
    var password: String = "",
    var message:String? = null,
    var isLoggedIn:Boolean = false
) {
    val loginEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
}