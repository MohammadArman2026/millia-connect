package com.reyaz.core.network.model

sealed class DownloadResult {
    data class Progress(val percent: Int? = null) : DownloadResult()
    data class Success(val filePath: String) : DownloadResult()
    data class Error(val exception: Throwable) : DownloadResult()
}
