package com.reyaz.wifiautoconnect.ui.screen

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/*
private suspend fun loginToCaptivePortal(username: String, password: String): Boolean {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("https://university-portal-login.com") // Replace with the actual URL
            .post(formBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: IOException) {
            false
        }
    }
}*/
