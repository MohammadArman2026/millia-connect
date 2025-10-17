package com.reyaz.feature.rent.domain.repository

import com.reyaz.feature.rent.domain.model.UploadResponse
import retrofit2.http.Url

interface ImageRepository {
    suspend fun getUrls(apiKey:String,image: String):Result<UploadResponse>
}