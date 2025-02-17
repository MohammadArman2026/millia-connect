package com.reyaz.wifiautoconnect.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.wifiautoconnect.data.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class VMLogin(
    private val userPreferences: UserPreferences
) : ViewModel() {
    init {
        viewModelScope.launch { Log.d("VM", userPreferences.username.first().toString()) }
    }
}

data class UiStateLogin(
    var username: String = "",
    var password: String = ""
)