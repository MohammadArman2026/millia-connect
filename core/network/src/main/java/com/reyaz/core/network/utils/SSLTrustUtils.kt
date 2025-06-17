package com.reyaz.core.network.utils

import android.util.Log // Keep android.util.Log if this is an Android Library module
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object SSLTrustUtils { // Using an object for utility functions
    private const val TAG = "SSLTrustUtils"

    /**
     * !!! WARNING: DO NOT USE IN PRODUCTION !!!
     * This method bypasses all SSL certificate validation, making your application vulnerable
     * to Man-in-the-Middle (MITM) attacks. It should only be used for debugging purposes
     * in development environments where security is not a concern.
     */
    fun trustAllHosts() {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            }
        )
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
//            Log.e(TAG, "!!! CRITICAL SECURITY WARNING: trustAllHosts() IS ACTIVE. DO NOT USE IN PRODUCTION APP !!!")
        } catch (e: Exception) {
            Log.e(TAG, "Error trusting all hosts: ${e.message}", e)
        }
    }
}