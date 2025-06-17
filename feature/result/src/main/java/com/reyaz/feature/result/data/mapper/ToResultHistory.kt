package com.reyaz.feature.result.data.mapper

import com.reyaz.feature.result.data.local.dto.CourseWithList
import com.reyaz.feature.result.domain.model.ResultHistory
import com.reyaz.feature.result.domain.model.ResultList

fun CourseWithList.toResultHistory(): ResultHistory {
    // Log.d(TAG, "Converting to ResultHistory")
    return ResultHistory(
        courseName = course.courseName,
        courseType = course.courseType,
        resultList = lists.map {
            ResultList(
                listId = it.listId,
                listTitle = it.remark,
                link = it.link,
                date = it.date.toString(),
                viewed = it.viewed
            )
        }
    )
}