package com.reyaz.feature.result.data.mapper

import com.reyaz.feature.result.data.local.dto.RemoteResultListDto
import com.reyaz.feature.result.data.local.entities.ResultListEntity
import java.util.Date

fun RemoteResultListDto.dtoListItemToEntity(courseId: String): ResultListEntity {
    return ResultListEntity(
        listId = srNo,
        remark = remark,
        viewed = false,
        link = link,
        listOwnerId = courseId,
        pdfPath = null,
        date = /*date*/ Date()         // TODO: replace the dto date
    )
}