package com.reyaz.feature.result.domain.repository

import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import com.reyaz.feature.result.domain.model.ResultHistory

interface ResultRepository {
    suspend fun getCourseTypes(): Result<List<CourseType>>
    suspend fun getCourses(type: String): Result<List<CourseName>>
    suspend fun getResult(type: String, course: String,  phdDepartment: String?): Result<List<ResultHistory>>
}