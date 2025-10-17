package com.reyaz.feature.rent.domain.model

data class UploadResponse(
    val data: ImgBBData,
    val success: Boolean,
    val status: Int
)

data class ImgBBData(
    val id: String,
    val title: String,
    val urlViewer: String,
    val url: String,
    val displayUrl: String,
    val width: String,
    val height: String,
    val size: String,
    val time: String,
    val expiration: String,
    val image: ImgBBImage,
    val thumb: ImgBBImage,
    val medium: ImgBBImage,
    val deleteUrl: String
)

data class ImgBBImage(
    val filename: String,
    val name: String,
    val mime: String,
    val extension: String,
    val url: String
)
