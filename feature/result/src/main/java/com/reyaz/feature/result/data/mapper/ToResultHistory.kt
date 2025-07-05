package com.reyaz.feature.result.data.mapper

import com.reyaz.core.common.utils.longToDateString
import com.reyaz.core.common.utils.toTimeAgoString
import com.reyaz.feature.result.data.local.dto.CourseWithList
import com.reyaz.feature.result.domain.model.ResultHistory
import com.reyaz.feature.result.domain.model.ResultItem

fun CourseWithList.toResultHistory(): ResultHistory {
    // Log.d(TAG, "Converting to ResultHistory")
    return ResultHistory(
        courseId = course.courseId,
        courseName = course.courseName,
        courseType = course.courseType,
        syncDate = course.lastSync?.toTimeAgoString(),
        latestListDate = lists.maxByOrNull { it.releaseDate ?: 0L }?.releaseDate?.longToDateString(),
        resultList = lists.sortedWith(
            compareByDescending {
                it.releaseDate ?: 0L
            }
        ).map {
            ResultItem(
                listId = it.listId,
                listTitle = it.remark,
                link = it.link,
                date = it.releaseDate?.longToDateString(),
                viewed = it.viewed,
                localPath = it.pdfPath,
                downloadProgress = it.downloadProgress
            )
        }
    )
}