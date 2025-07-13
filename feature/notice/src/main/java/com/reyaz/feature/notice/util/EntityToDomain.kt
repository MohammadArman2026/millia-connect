package com.reyaz.feature.notice.util

import com.reyaz.core.common.utils.toTimeAgoString
import com.reyaz.feature.notice.data.local.NoticeEntity
import com.reyaz.feature.notice.domain.model.Notice

fun NoticeEntity.entityToDomain() = Notice(
    title = title,
    link = link,
    path = path,
    progress = progress,
    isRead = isViewed,
    fetchedOn = createdOn.toTimeAgoString()
)