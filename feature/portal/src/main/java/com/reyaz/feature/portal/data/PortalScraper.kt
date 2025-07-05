package com.reyaz.feature.portal.data

import android.util.Log
import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.core.common.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlElement
import org.htmlunit.html.HtmlPage
import org.htmlunit.html.HtmlPasswordInput
import org.htmlunit.html.HtmlTextInput
import java.net.HttpURLConnection
import java.net.URL


class PortalScraper(
//    private val notificationHelper: NotificationHelper,
    private val networkManager: NetworkManager,
    private val webClient: WebClient
) {
    private val enableLogging = true
    private fun log(message: String) {
        if (enableLogging) Log.d(TAG, message)
    }

    fun performLogin(username: String, password: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading("Logging In"))

        // default login credentials for testing
        if (username == "99999" && password == "sssss") {
            delay(2_000)
            // Report captive portal dismissed for successful dummy login
            networkManager.reportCaptivePortalDismissed()
            emit(Resource.Success(data = "Successfully Logged in!"))
        }

        networkManager.bindToWifiNetwork()
        //notificationHelper.showNotification("performing login", "in weblogin manager")
        val loginUrl = "http://10.2.0.10:8090/login?dummy"
        try {
            val page: HtmlPage = webClient.getPage(loginUrl)
            val usernameField: HtmlTextInput =
                page.getFirstByXPath("//input[@type='text']")
            val passwordField: HtmlPasswordInput =
                page.getFirstByXPath("//input[@type='password']")
            val loginButton: HtmlElement =
                page.getFirstByXPath("/html/body/div[1]/form/div[3]/button")

            usernameField.valueAttribute = username
            passwordField.valueAttribute = password
            val responsePage: HtmlPage = loginButton.click()
            val pageText = responsePage.asNormalizedText()
            if (pageText.contains("Note: Please enter your valid credentials."))
                throw (Exception("Wrong Username or Password"))
            else {
                networkManager.resetNetworkBinding()
                networkManager.reportCaptivePortalDismissed()
                val isWifiPrimary = isJmiWifi(forceUseWifi = false)
                if (isWifiPrimary) {
                    log("Wifi is Primary")
                    emit(Resource.Success(data = "Successfully Logged in!"))
                } else {
                    log("Wifi is not Primary")
                    emit(Resource.Success(data = "Successfully Logged in!", message = "But you may not enjoy it because your internet is on!"))
                }
            }
        } catch (e: Exception) {
            log("Error: ${e.message}")
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun performLogout(): Result<String> = withContext(Dispatchers.IO) {
//                    return@withContext Result.failure<String>(Exception("dummy errorrr"))
//        return@withContext Result.success<String>("dummy errorrr")
        try {
            networkManager.bindToWifiNetwork()
            val logoutUrl = "http://10.2.0.10:8090/logout?dummy"
            val page: HtmlPage = webClient.getPage(logoutUrl)

            // Log.d("WebScrapingService", "Logout Response Page text\n: ${page.asNormalizedText()}")
//            Log.d("WebScrapingService", "Logout Response Page xml: ${page.asXml()}")
            return@withContext Result.success("Successfully Logged out!")
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun isJmiWifi(forceUseWifi: Boolean = true): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            log("inside isJmiWifi")
            val url = URL("http://10.92.0.3/cgi-bin/koha/opac-elogin.pl")
            if (forceUseWifi)
                networkManager.bindToWifiNetwork()
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 2000
            connection.connect()
            val responseCode = connection.responseCode
            log("Response code: $responseCode")
            connection.disconnect()
            val isJmiWifi = responseCode == 200 || responseCode == 302 // 302 if redirect to login
            //log( "JMI Wifi $isJmiWifi")
            isJmiWifi
        } catch (e: Exception) {
            log("Error: ${e.message}")
            false
        }
    }
}

private const val TAG = "PORTAL_SCRAPER"