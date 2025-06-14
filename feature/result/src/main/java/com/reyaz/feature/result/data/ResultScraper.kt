package com.reyaz.feature.result.data

import android.util.Log
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private const val TAG = "RESULT_SCRAPER"
class ResultScraper {

    val webClient = WebClient(BrowserVersion.CHROME).apply {
        options.isJavaScriptEnabled = true
        options.isCssEnabled = false
        options.isThrowExceptionOnScriptError = false
        options.isThrowExceptionOnFailingStatusCode = false
        options.isPrintContentOnFailingStatusCode = false
        options.isUseInsecureSSL = true  // This allows insecure SSL
        ajaxController = NicelyResynchronizingAjaxController()
        refreshHandler = WaitingRefreshHandler()

        // Configure SSL to accept all certificates
        configureSslContext()
    }

    private fun WebClient.configureSslContext() {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            this.options.sslClientCertificateStore
            this.options.sslInsecureProtocol = "TLSv1.2"
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring SSL context: $e")
        }
    }

    suspend fun fetchDegree() = withContext(Dispatchers.IO) {
        val url = "https://admission.jmi.ac.in/EntranceResults/UniversityResult"
        try {
            val page: HtmlPage = webClient.getPage(url)
            Log.d(TAG, "fetchDegree: ${page.asNormalizedText()}")
            // Process your page here
        } catch (e: Exception) {
            Log.e(TAG, "fetchDegree: $e")
        }
    }
}