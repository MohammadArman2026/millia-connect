package com.reyaz.feature.notice.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.reyaz.feature.notice.data.local.NoticeEntity
import com.reyaz.feature.notice.data.local.NoticeTypeEntity

data class NoticeWithList(
    @Embedded val notice: NoticeTypeEntity,
    @Relation(
        parentColumn = "noticeType",
        entityColumn = "listOwnerId"
    )
    val lists: List<NoticeEntity>
)
