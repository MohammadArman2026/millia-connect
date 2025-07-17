package com.reyaz.feature.portal.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyaz.feature.portal.presentation.PortalViewModel

@Composable
fun CaptivePortalDialogContent(
    modifier: Modifier = Modifier,
    viewModel: PortalViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    Box (
        modifier = modifier
            //.padding(top = 12.dp)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ){
//        Text(text = "${uiState.isLoggedIn}")
        if (!uiState.isJamiaWifi) {  // not JMI-WiFi
            NotJmiWifiComposable(
                onRetry = { viewModel.retry() },
            )
        } else if (uiState.isLoggedIn) {
            ConnectedComposable(
                onDisconnect = { viewModel.logout() },
                errorMsg = uiState.errorMsg
            )
        } else {    // login form
            LoginFormComposable(modifier = modifier, uiState, viewModel)
        }
    }
}

