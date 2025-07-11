package com.reyaz.feature.result.domain.repository

import com.reyaz.core.network.model.DownloadResult
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import com.reyaz.feature.result.domain.model.ResultHistory
import kotlinx.coroutines.flow.Flow

interface ResultRepository {
    fun observeResults(): Flow<List<ResultHistory>>
    suspend fun getCourseTypes(): Result<List<CourseType>>
    suspend fun getCourses(type: String): Result<List<CourseName>>
    suspend fun getResult(type: String, course: String, phdDepartment: String = ""): Result<Unit>
    suspend fun refreshLocalResults(shouldNotify: Boolean)
    suspend fun saveCourse(courseId: String, courseName: String, courseTypeId: String, courseType: String, phdDepartmentId: String? = null, phdDepartment: String? = null)
    suspend fun deleteCourse(courseId: String)
    suspend fun downloadPdf(url: String, listId: String, fileName: String)
    suspend fun deleteFileByPath(path: String, listId: String)
    suspend fun markCourseAsRead(courseId: String)
}