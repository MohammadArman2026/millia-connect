package com.reyaz.milliaconnect.ui.screen

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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

    val scope = rememberCoroutineScope()
    val webViewRef = remember { mutableStateOf<WebView?>(null) }
    val hasSubmitted = remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var autoLoginAttempted by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef.value?.destroy()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = uiState.baseUrl,
            onValueChange = { viewModel.updateBaseUrl(it)},
            label = { Text("Wifi Url") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.username,
            onValueChange = { viewModel.updateUsername(it) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                webViewRef.value?.let { webView ->
                    if (!isLoggedIn) {
                        loginScript(webView, uiState.username, uiState.password)
                        // Save credentials when user clicks login
                        scope.launch {
                            viewModel.saveCredentials()
                            viewModel.updateMessage("Credentials saved for auto-login")
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            enabled = uiState.loginEnabled
        ) {
            Text(if (isLoggedIn) "Logged In" else "Login")
        }

        //logout
        TextButton(
            onClick = {
                webViewRef.value?.let { webView ->
                    if (isLoggedIn) {
                        logoutScript(webView)
                        isLoggedIn = false
                    }
                }
            }
        ) {
            Text("Logout")
        }
        uiState.message?.let { Text(it) }

        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            webViewRef.value = view

                            // Try auto-login if credentials exist and haven't attempted yet
                            /*if (!autoLoginAttempted && username.isNotEmpty() && password.isNotEmpty()) {
                                view?.let {
                                    loginScript(it, username, password)
                                    autoLoginAttempted = true
                                }
                            }*/

                            // Extract current field values
                            extractFieldValues(view) { user, pass, loggedIn ->
                                viewModel.updateUsername(user)
                                viewModel.updatePassword(pass)
                                viewModel.updateMessage(if (loggedIn) "Successfully logged in" else null)
                            }
                        }
                    }
                    loadUrl(/*baseUrl*/"www.google.com")
                }
            }
        )
    }
}

private fun extractFieldValues(webView: WebView?, callback: (String, String, Boolean) -> Unit) {
    val jsCode = """
        (function() {
            const username = document.getElementById('ft_un')?.value || '';
            const password = document.getElementById('ft_pd')?.value || '';
            const loginBtn = document.querySelector('button.primary');
            const isLoggedIn = loginBtn ? loginBtn.textContent.toLowerCase().includes('logged in') : false;
            return JSON.stringify({username, password, isLoggedIn});
        })();
    """.trimIndent()

    webView?.evaluateJavascript(jsCode) { result ->
        try {
            val json = result.removeSurrounding("\"").replace("\\\"", "\"")
            val regex =
                """.*"username":"(.*)","password":"(.*)","isLoggedIn":(true|false).*""".toRegex()
            val match = regex.find(json)
            if (match != null) {
                val (user, pass, loggedIn) = match.destructured
                callback(user, pass, loggedIn.toBoolean())
            }
        } catch (e: Exception) {
            callback("", "", false)
        }
    }
}

private fun loginScript(webView: WebView, username: String, password: String) {
    val jsCode = """
        (function() {
            const unField = document.getElementById('ft_un');
            const pwField = document.getElementById('ft_pd');
            const loginBtn = document.querySelector('button.primary');
            
            if (unField && pwField && loginBtn) {
                unField.value = '$username';
                pwField.value = '$password';
                loginBtn.click();
                return true;
            }
            return false;
        })();
    """.trimIndent()

    webView.evaluateJavascript(jsCode, null)
}

private fun logoutScript(webView: WebView) {
    val jsCode = """
        (function() {
            const logoutBtn = document.querySelector('button.logout') || 
                             document.querySelector('a.logout') ||
                             Array.from(document.querySelectorAll('button')).find(b => b.textContent.toLowerCase().includes('logout'));
            if (logoutBtn) {
                logoutBtn.click();
                return true;
            }
            return false;
        })();
    """.trimIndent()

    webView.evaluateJavascript(jsCode, null)
}