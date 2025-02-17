package com.reyaz.wifiautoconnect.ui.screen

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(modifier: Modifier) {
    val hasSubmitted = remember { mutableStateOf(false) }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (!hasSubmitted.value) {
                            injectAutofillScript(view)
                            hasSubmitted.value = true
                        }
                    }
                }
                loadUrl("http://10.2.0.10:8090/login?")
            }
        }
    )
}

private fun injectAutofillScript(webView: WebView?, username: String = "202207696", password: String = "ique@7696595") {
    val jsCode = """
        document.getElementById("ft_un").value = "$username";
        document.getElementById("ft_pd").value = "$password";
        document.querySelector("button.primary").click();
    """.trimIndent()

    webView?.evaluateJavascript(jsCode, null)
}