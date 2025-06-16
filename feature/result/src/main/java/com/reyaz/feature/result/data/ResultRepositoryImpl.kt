package com.reyaz.feature.result.data

import android.util.Log
import com.reyaz.feature.result.data.local.dao.ResultDao
import com.reyaz.feature.result.data.local.dto.CourseWithList
import com.reyaz.feature.result.data.local.dto.RemoteResultListDto
import com.reyaz.feature.result.data.local.entities.CourseEntity
import com.reyaz.feature.result.data.local.entities.ResultListEntity
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import com.reyaz.feature.result.domain.model.ResultHistory
import com.reyaz.feature.result.domain.model.ResultList
import com.reyaz.feature.result.domain.repository.ResultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Date

private const val TAG = "RESULT_REPO_IMPL"

class ResultRepositoryImpl(
    private val resultScraper: ResultScraper,
    private val resultDao: ResultDao
//    private val resultScraper: DropdownSelector
) : ResultRepository {

    override fun observeResults(): Flow<List<ResultHistory>> {
//        Log.d(TAG, "Observing results")
        return resultDao.observeResults()
            .map { resultsEntity ->
                resultsEntity.forEach { course ->
                    Log.d(TAG, "Course: ${course.course.courseId}, Lists: ${course.lists}")
                }


                resultsEntity.map { it.toResultHistory() }
            }
    }

    override suspend fun getCourseTypes(): Result<List<CourseType>> =
        withContext(Dispatchers.IO) {
            try {
//                resultScraper.getProgramsForCourseType() // Java method
                val response: Result<List<CourseType>> =
                    resultScraper.fetchProgramTypes() // Java method
                Result.success(response.getOrDefault(emptyList()))
            } catch (e: Exception) {
                Log.e(TAG, "Exception while scraping: ${e.message}")
                Result.failure(e)
            }
        }

    override suspend fun getCourses(type: String): Result<List<CourseName>> =
        withContext(Dispatchers.IO) {
            try {
                val request: Result<List<CourseName>> = resultScraper.fetchPrograms(type)
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
    ): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            Log.d(TAG, "Getting Result for $type, $course, $phdDepartment")
            val result: Result<List<RemoteResultListDto>> = resultScraper.fetchResult(
                //                courseType = type,        // TODO: uncomment
                courseTypeId = "UG1",
                //                courseName = course,
                courseNameId = "B03",
                phdDisciplineId = phdDepartment
            )
            //            Result.success(result.getOrDefault(emptyList()))
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

    private fun fetchRemoteResultList(
        typeId: String,
        courseId: String,
        phdId: String
    ): Result<List<RemoteResultListDto>> {
        return try {
            val scrapedList: List<RemoteResultListDto> =
                resultScraper.fetchResult(
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
                    if ( newList.isNotEmpty() &&  newList.size != courseWithList.lists.size) {
                        newList.forEach {
                            resultDao.insertResultList(it.dtoListItemToEntity(courseId = courseWithList.course.courseId))
                        }
                    } else{
                        Log.d(TAG, "No new results found")
                    }
                } else {
                    Log.e(TAG, "Error fetching remote result list: ${newListResponse.exceptionOrNull()}")
                    throw Exception("Unable to fetch from remote!")
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing results: ${e.message}")
        }
    }

}

private fun RemoteResultListDto.dtoListItemToEntity(courseId: String): ResultListEntity {
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
