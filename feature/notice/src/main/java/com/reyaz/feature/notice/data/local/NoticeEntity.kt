package com.reyaz.feature.notice.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoticeEntity(
    @PrimaryKey
    val title: String,
    val link: String?,
    val createdOn: Long,
    val path: String? = null,
    val typeId: String,
    val isViewed: Boolean = true
)
