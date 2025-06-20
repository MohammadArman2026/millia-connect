package com.reyaz.milliaconnect1.ui.screen.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyaz.feature.portal.presentation.PortalUiState

@Composable
fun WifiIconComposable(
    portalUiState: PortalUiState,
    navigateToPortal: () -> Unit
) {
    IconButton(onClick = {
        navigateToPortal()
    }) {
        if (portalUiState.isLoading) {
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            Icon(
                imageVector =
                    if (portalUiState.isLoggedIn || portalUiState.isJamiaWifi)
                        Icons.Default.Wifi
                    else Icons.Default.WifiOff,
                contentDescription = "Wifi",
                tint = if (portalUiState.isLoggedIn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}