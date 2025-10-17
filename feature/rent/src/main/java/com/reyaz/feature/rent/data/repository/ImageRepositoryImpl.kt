package com.reyaz.feature.rent.data.repository

import com.reyaz.feature.rent.data.local.ImageApiService
import com.reyaz.feature.rent.domain.model.UploadResponse
import com.reyaz.feature.rent.domain.repository.ImageRepository

class ImageRepositoryImpl(private val imageApiService: ImageApiService) : ImageRepository {
    override suspend fun getUrls(
        apiKey: String,
        image: String
    ): Result<UploadResponse> {
        return try {
            val response = imageApiService.uploadImage(apiKey, image)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
