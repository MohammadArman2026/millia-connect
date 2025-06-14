package com.reyaz.feature.portal.data

import android.util.Log
import com.reyaz.core.common.utlis.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.htmlunit.BrowserVersion
import org.htmlunit.NicelyResynchronizingAjaxController
import org.htmlunit.WaitingRefreshHandler
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlElement
import org.htmlunit.html.HtmlPage
import org.htmlunit.html.HtmlPasswordInput
import org.htmlunit.html.HtmlTextInput
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class PortalScraper(
//    private val notificationHelper: NotificationHelper,
    private val networkManager: NetworkManager
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
                networkManager.reportCaptivePortalDismissed()
                return@withContext Result.success("Successfully Logged in!")
            }
            Log.d("WebScrapingService", "DUMMY login")
            networkManager.forceUseWifi()
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
                    networkManager.reportCaptivePortalDismissed()
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
            networkManager.forceUseWifi()
            val logoutUrl = "http://10.2.0.10:8090/logout?dummy"
            val page: HtmlPage = webClient.getPage(logoutUrl)

            Log.d("WebScrapingService", "Logout Response Page text\n: ${page.asNormalizedText()}")
//            Log.d("WebScrapingService", "Logout Response Page xml: ${page.asXml()}")
            return@withContext Result.success("Successfully Logged out!")
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun hasInternetAccess() = withContext(Dispatchers.IO) {
        return@withContext try {
            val url = URL("https://www.google.com/generate_204") // Lightweight endpoint
            networkManager.forceUseWifi()
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 2000
            connection.readTimeout = 2000
            connection.connect()
            val haveInternet = connection.responseCode == 204
            Log.d(TAG, "haveInternet $haveInternet")
            haveInternet
        } catch (e: IOException) {
            Log.d(TAG, "haveInternet false")
            //Log.e(TAG, "Error while connecting to Google", e)
            false
        }
    }

     suspend fun isJmiWifi(forceUseWifi: Boolean = true): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val url = URL("http://10.2.0.10:8090/login?dummy")
            if (forceUseWifi)
                networkManager.forceUseWifi()
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 2000
            connection.connect()
            val responseCode = connection.responseCode
            connection.disconnect()
            val isJmiWifi = responseCode == 200 || responseCode == 302 // 302 if redirect to login
            Log.d(TAG, "JMI Wifi $isJmiWifi")
            isJmiWifi
        } catch (e: IOException) {
            Log.d(TAG, "JMI Wifi false")
            //Log.e(TAG, "Error while connecting to JMI Wifi", e)
            false
        }
    }
}

private const val TAG = "PORTAL_SCRAPER"