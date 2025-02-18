package com.reyaz.milliaconnect.data

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlAnchor
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput
import com.gargoylesoftware.htmlunit.html.HtmlTextInput
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.userAgent
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.HttpURLConnection
import java.net.URL

class WebLoginManager() {
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
            val loginUrl = "http://10.2.0.10:8090/login?dummy"
            Log.d("WebScrapingService", "it is login portal")
            return@withContext isCaptivePortal()
                .fold(
                    onSuccess = { isLoggedIn ->
                        if (isLoggedIn) {
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

                                if (pageText.contains("Note: Please enter your valid credentials.")) {
                                    return@fold Result.failure(Exception("Wrong Username or Password"))
                                }

                                if (pageText.contains("This browser window is used to keep your authentication session active. Please leave it open in the background and open a new window to continue.") || pageText.contains(
                                        "Authentication refresh in 7200 seconds ..."
                                    )
                                ) {
                                    return@fold Result.success("Successfully Logged in!")
                                } else
                                    return@fold Result.failure(Exception("Unable to detect login status"))
                            } catch (e: Exception) {
                                Log.d("WebScrapingService", "3")
                                Log.e("WebScrapingService", "Error While Connecting", e)
                                return@fold Result.failure(e)
                            } finally {
                                webClient.close()
                            }
                        } else {
                            Log.d("WebScrapingService", "2")
                            return@fold Result.success("You are Already Connected")
                        }
                    },
                    onFailure = {
                        return@fold Result.success("You are not Connected to the internet")
                    }
                )
        }

    /*suspend fun performLogin(username: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
            val loginUrl = "http://10.2.0.10:8090/login?dummy"
            *//*try {
                val connection = URL(loginUrl).openConnection() as HttpURLConnection
                connection.connect()
                Log.d("WebScrapingService", "Response Code: ${connection.responseCode}")
                Log.d("WebScrapingService", "it is login portal")
            } catch (e: Exception) {
                Log.e("WebScrapingService", "Error connecting to login page", e)
                Log.e("WebScrapingService", "not university network", e)
            }*/
    /*

            isCaptivePortal()
                .onSuccess { isCaptive ->
                    if (isCaptive) {
                        try {
                            // Load the login page
                            val page: HtmlPage = webClient.getPage(loginUrl)

                            // Get input fields correctly
                            val usernameField: HtmlTextInput =
                                page.getFirstByXPath("//input[@type='text']")
                            val passwordField: HtmlPasswordInput =
                                page.getFirstByXPath("//input[@type='password']")  // Correct type
                            val loginButton: HtmlElement =
                                page.getFirstByXPath("/html/body/div[1]/form/div[3]/button")

                            // Fill the login form
                            usernameField.setValueAttribute(username)
                            passwordField.setValueAttribute(password)

                            // Submit the form
                            val responsePage: HtmlPage = loginButton.click()

                            Log.d(
                                "WebScrapingService",
                                "Response Page: ${responsePage.asNormalizedText()}"
                            )
                            Log.d("WebScrapingService", "Response Page: ${responsePage.asXml()}")

                            val pageText = responsePage.asNormalizedText()

                            if (pageText.contains("Note: Please enter your valid credentials.")) {
                                return@withContext Result.failure(Exception("Wrong Username or Password"))
                            }
                            Result.success("Successfully Logged in!")
                        } catch (e: Exception) {
                            Log.e("WebScrapingService", "Error While Connecting", e)
                            return@withContext Result.failure(e)
                        } finally {
                            webClient.close()
                        }
                    } else {
                        return@withContext Result.success("You are Already Connected")
                    }
                }
                .onFailure {
                    return@withContext Result.success("You are not Connected to the internet")
                }
            return@withContext Result.failure(Exception("Unknown Error"))
        }*/

    suspend fun performLogout(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val logoutUrl = "http://10.2.0.10:8090/logout?dummy"
            val page: HtmlPage = webClient.getPage(logoutUrl)

            if (page.asNormalizedText().contains("Successfully logged out")){
                return@withContext Result.success("Successfully Logged out!")
            }
//            Log.d("WebScrapingService", "Logout Response Page text\n: ${page.asNormalizedText()}")
//            Log.d("WebScrapingService", "Logout Response Page xml: ${page.asXml()}")
            Result.success("Successfully Logged out!")
        } catch (e: Exception) {
            Result.failure(e)
        }finally {
            webClient.close()
        }
    }

    /**
     * Checks if the current network is a captive portal.
     *
     * A captive portal is a web page that the user of a public-access network
     * is obliged to view and interact with before access is granted.
     *
     * This function attempts to connect to a specific Google URL
     * ("http://clients3.google.com/generate_204"). This URL is designed to return
     * an HTTP 204 No Content response when accessed over a normal internet
     * connection.
     *
     * If the response code is not 204, it suggests that the connection was
     * intercepted by a captive portal, which would redirect the request to
     * its login/terms of service page.
     *
     * @return `true` if a captive portal is detected, `false` otherwise.
     *         Returns `false` in case of any exception during the connection attempt.
     */
    private fun isCaptivePortal(): Result<Boolean> {
        return try {
            Log.d("WebScrapingService", "isCaptive checking...")
            val url = "http://clients3.google.com/generate_204"
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = false
            connection.connect()
            val responseCode = connection.responseCode
            connection.disconnect()
            Log.d("WebScrapingService", "Response Code: $responseCode")
            Result.success(responseCode != 204)  // If it is NOT 204, it's a captive portal. The Google server receives the request and responds with an HTTP status code of 204 (No Content)
        } catch (e: Exception) {
            Log.e("WebScrapingService", "Error connecting to Google", e)
            Result.failure(e)
        }
    }
}
