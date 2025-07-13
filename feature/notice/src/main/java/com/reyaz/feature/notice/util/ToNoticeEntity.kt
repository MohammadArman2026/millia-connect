package com.reyaz.feature.notice.util

import com.reyaz.feature.notice.data.local.NoticeEntity
import com.reyaz.feature.notice.data.model.NoticeDto

fun NoticeDto.toNoticeEntity(): NoticeEntity {
    return NoticeEntity(
        typeId = type.typeId,
        title = title,
        link = url,
        createdOn = createdOn,
        isViewed = false
    )
}