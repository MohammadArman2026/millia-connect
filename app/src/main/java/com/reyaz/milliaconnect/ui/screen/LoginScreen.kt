package com.reyaz.milliaconnect.ui.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.reyaz.milliaconnect.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    viewModel: VMLogin = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.millia_connect_logo),
                "logo",
                modifier = Modifier.fillMaxWidth(0.8f)
            )
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
                )
            )

            Row(
                modifier = Modifier
//                    .fillMaxWidth()
                    .padding( 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = (Arrangement.End)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = if (uiState.autoConnect) "Your device will automatically connect when session expired." else "Auto Connect",
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    lineHeight = 16.sp
                )
                Spacer(Modifier.width(16.dp))
                Switch(
                    checked = uiState.autoConnect,
                    onCheckedChange = { viewModel.updateAutoConnect(it, context) },
                    colors = SwitchDefaults.colors()
                )
            }

            // login btn
            Button(
                onClick = {
                    viewModel.handleLogin()
                },
                modifier = Modifier
//                    .padding(top = 16.dp)
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
        if (uiState.showNoWifiDialog) {
            BackHandler {
                // Exit the app when back button is pressed and the dialog is displayed
                (context as? Activity)?.finish()
            }
            Dialog(
                onDismissRequest = { /* viewModel.dismissNoWifiDialog() */ }
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
    }
}
