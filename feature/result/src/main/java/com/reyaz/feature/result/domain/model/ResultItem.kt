package com.reyaz.feature.result.domain.model

data class ResultItem(
    val listId: String = "",
    val listTitle: String = "",
    val link: String? = null,
    val date: String?,
    val localPath: String? = null,
    val downloadProgress: Int? = null,
    val viewed: Boolean = true
){
    val isDownloadable: Boolean = !link.isNullOrEmpty() && link.endsWith(".pdf")
}