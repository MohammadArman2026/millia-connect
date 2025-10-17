package com.reyaz.feature.rent.data.local

import com.reyaz.feature.rent.domain.model.UploadResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ImageApiService {

        @FormUrlEncoded
        @POST("1/upload")
        suspend fun uploadImage(
            @Field("key") apiKey: String,
            @Field("image") base64Image: String,
        ): Response<UploadResponse>

}