package com.reyaz.feature.notice.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoticeEntity(
    @PrimaryKey
    val noticeId: String,   // todo
    val typeName: String,
    val title: String,
    val date: String,
    val path: String?,
    val link: String?,
    val listOwnerId: String,
)
