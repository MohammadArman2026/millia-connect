package com.reyaz.feature.portal.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.reyaz.feature.portal.presentation.PortalViewModel

@Composable
fun CaptivePortalDialogContent(
    modifier: Modifier = Modifier,
    viewModel: PortalViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.padding(horizontal = 32.dp, vertical = 32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = uiState.username,
            onValueChange = { viewModel.updateUsername(it) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    viewModel.handleLogin()
                }
            )
        )

        Row(
            modifier = Modifier
//                    .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = (Arrangement.End)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = if (uiState.autoConnect) "Your device will automatically connect when session expired." else "Auto Connect",
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 12.sp
            )
            Spacer(Modifier.width(16.dp))
            Switch(
                modifier = Modifier,
                checked = uiState.autoConnect,
                onCheckedChange = { viewModel.updateAutoConnect(it, context) },
                colors = SwitchDefaults.colors()
            )
        }

        // login btn
        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.handleLogin()
            },
            modifier = Modifier
                .height(48.dp)
                .padding(16.dp, 12.dp, 16.dp, 0.dp)
//                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            enabled = uiState.loginEnabled
        ) {
            Text("Login")
        }
        //logout
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.isLoggedIn,
            onClick = { viewModel.logout() }) {
            Text("Logout")
        }

        Text(
            text = uiState.message,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1.2f))
    }
    uiState.loadingMessage?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                .clickable(enabled = false, role = Role.Button, onClick = {}),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier
            )
            Text(it, modifier = Modifier.padding(top = 16.dp))
        }
    }
    if (uiState.showNoWifiDialog)
        Dialog(
            onDismissRequest = {

            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Wifi Not Connected",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Please connect JMI Wifi and try again.",
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
}