package com.reyaz.wifiautoconnect.ui.screen

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
import androidx.datastore.preferences.preferencesDataStore
import com.reyaz.wifiautoconnect.data.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun WebViewScreen(modifier: Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }
    val webViewRef = remember { mutableStateOf<WebView?>(null) }
    val hasSubmitted = remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var baseUrl by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var autoLoginAttempted by remember { mutableStateOf(false) }

    // Load saved credentials when screen starts
    LaunchedEffect(Unit) {
        delay(1000)
        username = userPreferences.username.first()
        password = userPreferences.password.first()
        baseUrl = userPreferences.baseUrl.first()
        Log.d("WebViewScreen", "Loaded credentials: $username, $password")
    }

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
            value = baseUrl,
            onValueChange = { baseUrl = it },
            label = { Text("Wifi Url") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                webViewRef.value?.let { webView ->
                    if (!isLoggedIn) {
                        loginScript(webView, username, password)
                        // Save credentials when user clicks login
                        scope.launch {
                            userPreferences.saveCredentials(username, password)
                            message = "Credentials saved for auto-login"
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
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
        message?.let { Text(it) }

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
                            if (!autoLoginAttempted && username.isNotEmpty() && password.isNotEmpty()) {
                                view?.let {
                                    loginScript(it, username, password)
                                    autoLoginAttempted = true
                                }
                            }

                            // Extract current field values
                            extractFieldValues(view) { user, pass, loggedIn ->
                                username = user
                                password = pass
                                isLoggedIn = loggedIn
                                message = if (loggedIn) "Successfully logged in" else null
                            }
                        }
                    }
                    loadUrl(baseUrl)
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