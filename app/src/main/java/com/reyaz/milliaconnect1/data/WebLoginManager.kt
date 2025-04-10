package com.reyaz.milliaconnect1.data

import android.content.Intent
import android.net.CaptivePortal
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.Log
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput
import com.gargoylesoftware.htmlunit.html.HtmlTextInput
import com.reyaz.milliaconnect1.util.NetworkConnectivityObserver
import com.reyaz.milliaconnect1.util.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class WebLoginManager(
    private val notificationHelper: NotificationHelper,
    private val wifiNetworkManager: NetworkConnectivityObserver
) {
    val webClient = WebClient(BrowserVersion.CHROME).apply {
        options.isJavaScriptEnabled = true
        options.isCssEnabled = false
        options.isThrowExceptionOnScriptError = false
        options.isThrowExceptionOnFailingStatusCode = false
        options.isPrintContentOnFailingStatusCode = false
        ajaxController = NicelyResynchronizingAjaxController()
        refreshHandler = WaitingRefreshHandler()
    }

    suspend fun performLogin(username: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
//            return@withContext Result.failure<String>(Exception("dummy errorrr"))
//            return@withContext Result.success<String>("Successfully Logged in!")

            Log.d("WebScrapingService", "DUMMY login begins")
            if (username == "99999" && password == "sssss") {
                delay(2_000)
                // Report captive portal dismissed for successful dummy login
                wifiNetworkManager.reportCaptivePortalDismissed()
                return@withContext Result.success("Successfully Logged in!")
            }
            Log.d("WebScrapingService", "DUMMY login")
            wifiNetworkManager.forceUseWifi()
            //notificationHelper.showNotification("performing login", "in weblogin manager")
            val loginUrl = "http://10.2.0.10:8090/login?dummy"
//            Log.d("WebScrapingService", "it is login portal")
            try {
                Log.d("WebScrapingService", "1")
                val page: HtmlPage = webClient.getPage(loginUrl)
                val usernameField: HtmlTextInput =
                    page.getFirstByXPath("//input[@type='text']")
                val passwordField: HtmlPasswordInput =
                    page.getFirstByXPath("//input[@type='password']")
                val loginButton: HtmlElement =
                    page.getFirstByXPath("/html/body/div[1]/form/div[3]/button")
                Log.d("WebScrapingService", "$username, $password")

                usernameField.setValueAttribute(username)
                passwordField.setValueAttribute(password)
                val responsePage: HtmlPage = loginButton.click()
//                                delay(2000)
                val pageText = responsePage.asNormalizedText()
                // Closing the webClient to release resources.
                // The WebClient is used to simulate a web browser.
                // It is important to close it after use to free up resources.
                Log.d("WebScrapingService", "Response Page: $pageText")
                if (pageText.contains("Note: Please enter your valid credentials."))
                    Result.failure(Exception("Wrong Username or Password"))
                else {
                    // Report captive portal dismissed when login is successful
                    wifiNetworkManager.reportCaptivePortalDismissed()
                    Result.success("Successfully Logged in!")
                }
            } catch (e: Exception) {
                Log.d("WebScrapingService", "3")
                Log.e("WebScrapingService", "Error While Connecting", e)
                Result.failure(e)
            }
        }

    suspend fun performLogout(): Result<String> = withContext(Dispatchers.IO) {
//                    return@withContext Result.failure<String>(Exception("dummy errorrr"))
//        return@withContext Result.success<String>("dummy errorrr")
        try {
            wifiNetworkManager.forceUseWifi()
            val logoutUrl = "http://10.2.0.10:8090/logout?dummy"
            val page: HtmlPage = webClient.getPage(logoutUrl)

            Log.d("WebScrapingService", "Logout Response Page text\n: ${page.asNormalizedText()}")
//            Log.d("WebScrapingService", "Logout Response Page xml: ${page.asXml()}")
            return@withContext Result.success("Successfully Logged out!")
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}
