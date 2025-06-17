package com.reyaz.feature.result.data

import android.util.Log
import com.reyaz.core.common.utlis.safeCall
import com.reyaz.core.common.utlis.safeSuspendCall
import com.reyaz.feature.result.data.local.dao.ResultDao
import com.reyaz.feature.result.data.local.dto.RemoteResultListDto
import com.reyaz.feature.result.data.local.entities.CourseEntity
import com.reyaz.feature.result.data.mapper.dtoListItemToEntity
import com.reyaz.feature.result.data.mapper.toResultHistory
import com.reyaz.feature.result.data.scraper.ResultApiService
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import com.reyaz.feature.result.domain.model.ResultHistory
import com.reyaz.feature.result.domain.repository.ResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val TAG = "RESULT_REPO_IMPL"

class ResultRepositoryImpl(
    private val resultApi: ResultApiService,
    private val resultDao: ResultDao
) : ResultRepository {

    override fun observeResults(): Flow<List<ResultHistory>> {
        return resultDao.observeResults()
            .map { resultsEntity ->
                resultsEntity.map { it.toResultHistory() }
            }
    }

    override suspend fun getCourseTypes(): Result<List<CourseType>> =
        safeSuspendCall { resultApi.fetchProgramTypes().getOrDefault(emptyList()) }


    override suspend fun getCourses(type: String): Result<List<CourseName>> {
        return try {
            val request: Result<List<CourseName>> = resultApi.fetchPrograms(type)
            if (request.isSuccess) {
                Result.success(request.getOrDefault(emptyList()))
            } else {
                throw request.exceptionOrNull()!!
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveCourse(
        courseId: String,
        courseName: String,
        courseTypeId: String,
        courseType: String,
        phdDepartmentId: String?,
        phdDepartment: String?
    ) {
        resultDao.insertCourse(
            course = CourseEntity(
                courseId = courseId,
                courseName = courseName,
                courseType = courseType,
                courseTypeId = courseTypeId,
                phdDepartmentId = phdDepartmentId,
                phdDepartment = phdDepartment
            )
        )
    }

    override suspend fun getResult(
        type: String,
        course: String,
        phdDepartment: String
    ): Result<Unit> {
        return try {
//            val result: Result<List<RemoteResultListDto>> = resultApi.fetchResult(courseTypeId = "UG1", courseNameId = "B03", phdDisciplineId = phdDepartment)
            val remoteList =
                fetchRemoteResultList(typeId = type, courseId = course, phdId = phdDepartment)
            if (remoteList.isSuccess) {
                remoteList.getOrDefault(emptyList()).map { it ->
                    resultDao.insertResultList(it.dtoListItemToEntity(courseId = course))
                }
            } else {
                Log.d(TAG, "Error fetching remote result list: ${remoteList.exceptionOrNull()}")
                throw Exception("Unable to fetch from remote!")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun fetchRemoteResultList(
        typeId: String,
        courseId: String,
        phdId: String
    ): Result<List<RemoteResultListDto>> {
        return try {
            val scrapedList: List<RemoteResultListDto> =
                resultApi.fetchResult(
                    courseTypeId = typeId,
                    courseNameId = courseId,
                    phdDisciplineId = phdId
                ).getOrThrow()
            Log.d(TAG, "Scraped list size: ${scrapedList.size}")
            Result.success(scrapedList)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching remote result list: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun refreshLocalResults() {
        try {
            // Log.d(TAG, "Refreshing local results")
            resultDao.observeResults().first().forEach { courseWithList ->

                val newListResponse = fetchRemoteResultList(
                    typeId = courseWithList.course.courseType,
                    courseId = courseWithList.course.courseName,
                    phdId = courseWithList.course.phdDepartment ?: ""
                )
                if (newListResponse.isSuccess) {
                    val newList = newListResponse.getOrDefault(emptyList())
                    if (newList.isNotEmpty() && newList.size != courseWithList.lists.size) {
                        newList.forEach {
                            resultDao.insertResultList(it.dtoListItemToEntity(courseId = courseWithList.course.courseId))
                        }
                    } else {
                        Log.d(TAG, "No new results found")
                    }
                } else {
                    Log.e(
                        TAG,
                        "Error fetching remote result list: ${newListResponse.exceptionOrNull()}"
                    )
                    throw Exception("Unable to fetch from remote!")
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing results: ${e.message}")
        }
    }

}

