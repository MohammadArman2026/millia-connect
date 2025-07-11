package com.reyaz.milliaconnect1.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiStatusbarConnectedNoInternet4
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiFind
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reyaz.feature.portal.presentation.PortalUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiIconComposable(
    portalUiState: PortalUiState,
    navigateToPortal: () -> Unit
) {
    val tooltipState = rememberTooltipState(isPersistent = false)
    LaunchedEffect(portalUiState.isWifiPrimary) {
        if (!portalUiState.isWifiPrimary) {
            tooltipState.show()
        }
    }
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip(
                modifier = Modifier.padding(top = 16.dp, end = 8.dp)
            ) {
                Text(
                    text =
                        "JMI-Wifi is not a\nPrimary connection.",
                    textAlign = TextAlign.Center
                )
            }
        },
        state = tooltipState
    ) {
        IconButton(onClick = {
            navigateToPortal()
        }) {
            /*if (portalUiState.isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )
            } else */
            Icon(
                imageVector =
                    if (portalUiState.isLoading) Icons.Default.WifiFind
                    else if (!portalUiState.isWifiPrimary) Icons.Default.SignalWifiStatusbarConnectedNoInternet4
                    else if (portalUiState.isLoggedIn || portalUiState.isJamiaWifi) Icons.Default.Wifi
                    else Icons.Default.WifiOff,
                contentDescription = "Wifi",
                tint = if (portalUiState.isLoggedIn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}