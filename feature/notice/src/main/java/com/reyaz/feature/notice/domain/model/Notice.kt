package com.reyaz.feature.notice.domain.model

data class Notice(
    val title : String?,
    val link : String?,
    val path : String?,
    val progress: Int?,
    val isRead : Boolean,
    val fetchedOn: String
)
