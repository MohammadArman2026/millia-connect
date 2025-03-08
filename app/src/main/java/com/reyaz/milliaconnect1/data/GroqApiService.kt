/*
package com.reyaz.milliaconnect.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

const val API_KEY = "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"
const val BASE_URL = "https://api.groq.com/"  // Replace with Cohere if using Cohere

interface GroqApiService {
    @Headers("Authorization: Bearer $API_KEY", "Content-Type: application/json")
    @POST("v1/chat/completions")  // Adjust endpoint based on API
    suspend fun classifySentence(@Body request: Map<String, Any>): Response
}

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)
*/
