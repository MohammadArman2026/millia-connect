package com.reyaz.feature.result.data.mapper

import com.reyaz.core.common.utils.dateStringToLong
import com.reyaz.feature.result.data.local.dto.RemoteResultListDto
import com.reyaz.feature.result.data.local.entities.ResultListEntity

fun RemoteResultListDto.dtoListItemToEntity(courseId: String, isViewed: Boolean = true): ResultListEntity {
    return ResultListEntity(
        listId = srNo,
        remark = remark,
        viewed = isViewed,
        link = link,
        listOwnerId = courseId,
        pdfPath = null,
        releaseDate = date.dateStringToLong()
    )
}