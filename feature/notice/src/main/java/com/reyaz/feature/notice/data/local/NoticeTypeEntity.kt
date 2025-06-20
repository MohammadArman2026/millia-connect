package com.reyaz.feature.notice.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoticeTypeEntity(
    @PrimaryKey
    val noticeType: String,
    val typeName: String,
    val sourceUrl: String,
)
