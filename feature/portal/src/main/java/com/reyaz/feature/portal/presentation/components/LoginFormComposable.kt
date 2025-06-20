package com.reyaz.feature.portal.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.portal.presentation.PortalUiState
import com.reyaz.feature.portal.presentation.PortalViewModel

@Composable
internal fun LoginFormComposable(
    modifier: Modifier,
    uiState: PortalUiState,
    viewModel: PortalViewModel
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier.padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(
            modifier = Modifier.height(42.dp),
            value = uiState.username,
            onValueChange = { viewModel.updateUsername(it) },
            label = "Username",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    viewModel.handleLogin()
                }
            )

        )
        CustomTextField(
            modifier = Modifier.height(42.dp),
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = "Password",
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
            modifier = Modifier,
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

        Column{// login btn
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.handleLogin()
                },
                modifier = Modifier
                    .height(48.dp)
                    .padding(4.dp, 12.dp, 4.dp, 0.dp)
//                .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                enabled = uiState.loginEnabled
            ) {
                Text("Login")
            }

            //logout btn
            OutlinedButton(
                modifier = Modifier
                    .height(48.dp)
                    .padding(4.dp, 12.dp, 4.dp, 0.dp)
                    .fillMaxWidth(),
                enabled = uiState.isLoggedIn,
                onClick = { viewModel.logout() }) {
                Text("Logout")
            }
        }

        uiState.message?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
//                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                //modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
        //Spacer(Modifier.weight(1.2f))
    }
}