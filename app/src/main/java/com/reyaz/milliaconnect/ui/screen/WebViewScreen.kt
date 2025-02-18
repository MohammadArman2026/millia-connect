package com.reyaz.milliaconnect.ui.screen

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.reyaz.milliaconnect.R
import com.reyaz.milliaconnect.data.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    viewModel: VMLogin = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
            /* OutlinedTextField(
                 value = uiState.baseUrl,
                 onValueChange = { viewModel.updateBaseUrl(it)},
                 label = { Text("Wifi Url") },
                 modifier = Modifier.fillMaxWidth()
             )*/
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
            // login btn
            Button(
                onClick = {
                    viewModel.handleLogin()
                },
                modifier = Modifier
                    .padding(top = 16.dp)
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

            uiState.message?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
            Spacer(Modifier.weight(1.2f))

            /*AndroidView(
                modifier = modifier.padding(top = 100.dp),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                webViewRef.value = view

                                // Try auto-login if credentials exist and haven't attempted yet
                                *//*if (!autoLoginAttempted && username.isNotEmpty() && password.isNotEmpty()) {
                                view?.let {
                                    loginScript(it, username, password)
                                    autoLoginAttempted = true
                                }
                            }

                            // Extract current field values
                            extractFieldValues(view) { user, pass, loggedIn ->
                                viewModel.updateUsername(user)
                                viewModel.updatePassword(pass)
                                viewModel.updateMessage(if (loggedIn) "Successfully logged in" else null)
                            }*//*
                        }
                    }
                    loadUrl(*//*baseUrl*//*"www.google.com")
                }
            }
        )*/
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
    }
}