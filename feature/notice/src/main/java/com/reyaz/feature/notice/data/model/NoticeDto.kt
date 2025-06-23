package com.reyaz.feature.notice.data.model

import java.util.Date

data class NoticeDto(
    val title: String,
    val url: String,
    val type: NoticeType,
    val createdOn: Long = Date().time,
)
