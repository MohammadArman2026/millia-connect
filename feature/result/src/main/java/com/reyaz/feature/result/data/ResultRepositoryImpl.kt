package com.reyaz.feature.result.data

import android.annotation.SuppressLint
import android.util.Log
import com.reyaz.core.common.utlis.safeSuspendCall
import com.reyaz.core.network.PdfManager
import com.reyaz.core.network.model.DownloadResult
import com.reyaz.core.notification.manager.AppNotificationManager
import com.reyaz.core.notification.model.NotificationData
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
import com.reyaz.feature.result.util.NotificationConstant
import com.reyaz.feature.result.worker.WorkScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private const val TAG = "RESULT_REPO_IMPL"

class ResultRepositoryImpl(
    private val resultApi: ResultApiService,
    private val resultDao: ResultDao,
    private val pdfDownloadResult: PdfManager,
    private val notificationManager: AppNotificationManager,
    private val workScheduler: WorkScheduler
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

    override suspend fun deleteCourse(courseId: String) {
        resultDao.deleteCourse(courseId)
        workScheduler.cancelWork(tag = courseId)
    }

    override suspend fun getResult(
        type: String,
        course: String,
        phdDepartment: String
    ): Result<Unit> {
        return try {
//            val result: Result<List<RemoteResultListDto>> = resultApi.fetchResult(courseTypeId = "UG1", courseNameId = "B03", phdDisciplineId = phdDepartment)
            val isExist: Boolean = resultDao.courseExist(courseId = course)
            if (!isExist) {
                val remoteList =
                    fetchRemoteResultList(typeId = type, courseId = course, phdId = phdDepartment)
                if (remoteList.isSuccess) {
                    remoteList.getOrDefault(emptyList()).map { it ->
                        resultDao.insertResultList(it.dtoListItemToEntity(
                            courseId = course,
                            isViewed = true
                        ))
                        workScheduler.schedulePeriodic(workName = course)
                    }
                } else {
                    Log.d(TAG, "Error fetching remote result list: ${remoteList.exceptionOrNull()}")
                    throw Exception("Unable to fetch from remote!")
                }
            } else {
                Log.d(TAG, "Course already being tracked!!")
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
            // Log.d(TAG, "Scraped list size: ${scrapedList.size}")
            Result.success(scrapedList)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching remote result list: ${e.message}")
            Result.failure(e)
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun refreshLocalResults() {
        try {
            Log.d(TAG, "Refreshing local results")
            resultDao.observeResults().first().forEach { courseWithList ->

                val newListResponse = fetchRemoteResultList(
                    typeId = courseWithList.course.courseType,
                    courseId = courseWithList.course.courseName,
                    phdId = courseWithList.course.phdDepartment ?: ""
                )
                if (newListResponse.isSuccess) {
                    val newList : List<RemoteResultListDto> = newListResponse.getOrDefault(emptyList())
                    if (newList.isNotEmpty() && newList.size != courseWithList.lists.size) {
                        newList.forEach {it->
                            resultDao.insertResultList(it.dtoListItemToEntity(courseId = courseWithList.course.courseId, isViewed = false))
                            try {
                                notificationManager.showNotification(
                                    NotificationData(
                                        id = it.srNo.toInt(),
                                        title = it.courseName,
                                        message = it.remark,
                                        channelId = NotificationConstant.RESULT_CHANNEL.channelId,
                                        channelName = NotificationConstant.RESULT_CHANNEL.channelName,
                                    )
                                )
                            } catch (e: Exception) {
                                Log.d(TAG, "Permission not granted")
                            }
                        }
                    } else {
                         Log.d(TAG, "No new results found")
                        /*try {
                            notificationManager.showNotification(
                                NotificationData(
                                    id = 0,
                                    title = "No new results found",
                                    message = "No new results found for ${courseWithList.course.courseName}",
                                    channelName = NotificationConstant.RESULT_CHANNEL.channelName,
                                    channelId = NotificationConstant.RESULT_CHANNEL.channelId
                                )
                            )
                        } catch (e: Exception) {
                            Log.d(TAG, "Permission not granted")
                        }*/
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

    override suspend fun downloadPdf(
        url: String,
        listId: String,
        fileName: String
    ): Flow<DownloadResult> = flow {
        // Log.d(TAG, "Download url: $url")
        pdfDownloadResult.downloadPdf(url = url, fileName = fileName).collect { downloadStatus ->
            // Log.d(TAG, "Download status: $downloadStatus")
            when (downloadStatus) {
                is DownloadResult.Error -> {
                    // Log.d(TAG, "Download error: ${downloadStatus.exception}")
                    resultDao.updatePdfPath(
                        path = null,
                        listId = listId,
                        progress = null
                    )
                    emit(DownloadResult.Error(downloadStatus.exception))
                }

                is DownloadResult.Progress -> {
                    // Log.d(TAG, "Download progress: ${downloadStatus.percent}")
                    resultDao.updateDownloadProgress(
                        progress = downloadStatus.percent,
                        listId = listId
                    )
                    emit(DownloadResult.Progress(downloadStatus.percent))
                }

                is DownloadResult.Success -> {

                    resultDao.updatePdfPath(
                        path = downloadStatus.filePath,
                        listId = listId,
                        progress = 100
                    )
                    // Log.d(TAG, "Download path: ${downloadStatus.filePath}")
                    emit(DownloadResult.Success(filePath = downloadStatus.filePath))
                }
            }
        }
    }

    override suspend fun deleteFileByPath(path: String, listId: String) {
        pdfDownloadResult.deleteFile(path)
        resultDao.updatePdfPath(path = null, listId = listId, progress = null)
        // Log.d(TAG, "path deleted from room")
    }

    override suspend fun markCourseAsRead(courseId: String) {
        resultDao.markCourseAsRead(courseId)
    }
}

