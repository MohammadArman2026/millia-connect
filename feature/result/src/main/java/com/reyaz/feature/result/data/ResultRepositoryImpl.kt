package com.reyaz.feature.result.data

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.net.toUri
import com.reyaz.core.common.utils.safeSuspendCall
import com.reyaz.core.network.PdfManager
import com.reyaz.core.network.model.DownloadResult
import com.reyaz.core.notification.manager.AppNotificationManager
import com.reyaz.core.notification.model.NotificationData
import com.reyaz.feature.result.data.local.dao.ResultDao
import com.reyaz.feature.result.data.local.dto.CourseWithList
import com.reyaz.feature.result.data.local.dto.RemoteResultListDto
import com.reyaz.feature.result.data.local.entities.CourseEntity
import com.reyaz.feature.result.data.mapper.dtoListItemToEntity
import com.reyaz.feature.result.data.mapper.toResultHistory
import com.reyaz.feature.result.data.scraper.ResultApiService
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import com.reyaz.feature.result.domain.model.ResultHistory
import com.reyaz.feature.result.domain.repository.ResultRepository
import com.reyaz.core.notification.utils.NotificationConstant
import constants.NavigationRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Date

private const val TAG = "RESULT_REPO_IMPL"

class ResultRepositoryImpl(
    private val resultApi: ResultApiService,
    private val context: Context,
    private val resultDao: ResultDao,
    private val pdfDownloadResult: PdfManager,
    private val notificationManager: AppNotificationManager,
) : ResultRepository {

    override fun observeResults(): Flow<List<ResultHistory>> {
        return resultDao.observeResults().map { courseWithLists ->
            courseWithLists.sortedWith(
                compareByDescending<CourseWithList> {
                    // Priority 1: has any un-viewed list
                    it.lists.any { list -> !list.viewed }
                }.thenByDescending {
                    // Priority 2: latest releaseDate from list
                    it.lists.maxOfOrNull { list -> list.releaseDate ?: 0L } ?: 0L
                }.thenByDescending {
                    // Priority 3: course lastSync
                    it.course.lastSync ?: 0L
                }
            ).map { it.toResultHistory() }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getCourseTypes(): Result<List<CourseType>> =
        safeSuspendCall { resultApi.fetchProgramTypes().getOrThrow() }

    override suspend fun getCourses(type: String): Result<List<CourseName>> {
        return try {
            val request: Result<List<CourseName>> = resultApi.fetchPrograms(type)
            if (request.isSuccess) {
                Result.success(request.getOrDefault(emptyList()))
            } else {
                throw request.exceptionOrNull() ?: Exception("Unknown error")
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
                phdDepartment = phdDepartment,
                lastSync = Date().time
            )
        )
    }

    override suspend fun deleteCourse(courseId: String) {
        resultDao.deleteCourse(courseId)
//        workScheduler.cancelWork(tag = courseId)
    }

    override suspend fun getResult(
        type: String,
        course: String,
        phdDepartment: String
    ): Result<Unit> {
        return try {
//            val result: Result<List<RemoteResultListDto>> = resultApi.fetchResult(courseTypeId = "UG1", courseNameId = "B03", phdDisciplineId = phdDepartment)
            val isExist: Boolean =
                withContext(Dispatchers.IO) { resultDao.havePreviousResult(courseId = course) }
            if (!isExist) {
                val remoteList =
                    fetchRemoteResultList(typeId = type, courseId = course, phdId = phdDepartment)
                if (remoteList.isSuccess) {
                    remoteList.getOrDefault(emptyList()).forEach { it ->
                        resultDao.insertResultList(
                            it.dtoListItemToEntity(
                                courseId = course,
                                isViewed = false
                            )
                        )
                    }
                    ResultFetchWorker.schedulePeriodicWork(context)
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
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun refreshLocalResults(shouldNotify: Boolean) {
        try {
            notificationManager.showNotification(
                NotificationData(
                    id = "refreshing".hashCode(),
                    title = "Refreshing results",
                    message = "Refreshing...",
                    channelName = NotificationConstant.RESULT_CHANNEL.channelName,
                    channelId = NotificationConstant.RESULT_CHANNEL.channelId,
                    destinationUri = NavigationRoute.Result.getDeepLink().toUri()
                )
            )
            Log.d(TAG, "Refreshing local results")
            val trackedCourse = resultDao.observeResults().first()
            trackedCourse.forEach { courseWithList ->
                Log.d(TAG, "Refreshing course: ${courseWithList.course.courseId}")
                val newListResponse = fetchRemoteResultList(
                    typeId = courseWithList.course.courseTypeId,
                    courseId = courseWithList.course.courseId,
                    phdId = courseWithList.course.phdDepartmentId ?: ""
                )
                if (newListResponse.isSuccess) {
                    val newList: List<RemoteResultListDto> =
                        newListResponse.getOrDefault(emptyList())
                    if (newList.isNotEmpty() && newList.size != courseWithList.lists.size) {
                        newList.forEach { remoteResultListDto ->
                            Log.d(TAG, "New result found: ${remoteResultListDto.courseName}")
                            withContext(Dispatchers.IO) {
                                resultDao.insertResultList(
                                    remoteResultListDto.dtoListItemToEntity(
                                        courseId = courseWithList.course.courseId,
                                        isViewed = false
                                    )
                                )
                            }
                            try {
                                if (shouldNotify)
                                    notificationManager.showNotification(
                                        NotificationData(
                                            id = remoteResultListDto.hashCode(),
                                            title = "Result released for ${remoteResultListDto.courseName/*.take(15)*/}...",
                                            message = remoteResultListDto.remark,
                                            channelId = NotificationConstant.RESULT_CHANNEL.channelId,
                                            channelName = NotificationConstant.RESULT_CHANNEL.channelName,
                                            destinationUri = NavigationRoute.Result.getDeepLink().toUri()
                                        )
                                    )
                            } catch (e: Exception) {
                                Log.d(TAG, "Permission not granted")
                            }
                        }
                    } else {
                        Log.d(TAG, "No new results found")
                        try {
                            if (shouldNotify)
                                notificationManager.showNotification(
                                    NotificationData(
                                        id = courseWithList.course.hashCode(),
                                        title = "No new results found",
                                        message = "No new results found for ${courseWithList.course.courseName}",
                                        channelName = NotificationConstant.RESULT_CHANNEL.channelName,
                                        channelId = NotificationConstant.RESULT_CHANNEL.channelId,
                                        destinationUri = NavigationRoute.Result.getDeepLink().toUri()
                                    )
                                )
                        } catch (e: Exception) {
                            Log.d(TAG, "Permission not granted")
                        }
                    }
                    withContext(Dispatchers.IO) {
                        resultDao.updateLastFetchedDate(courseId = courseWithList.course.courseId)
                    }
                } else {
                    Log.e(TAG, "Error fetching remote result list: ${newListResponse.exceptionOrNull()}")
                    //throw Exception("Unable to fetch from remote!")
                }
            }
            if (trackedCourse.isNotEmpty()) ResultFetchWorker.cancel(context = context)
        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing results: ${e.message}")
            throw e
        }
    }

    override suspend fun downloadPdf(
        url: String,
        listId: String,
        fileName: String
    ) {
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
                    //emit(DownloadResult.Error(downloadStatus.exception))
                }

                is DownloadResult.Progress -> {
                    // Log.d(TAG, "Download progress: ${downloadStatus.percent}")
                    resultDao.updateDownloadProgress(
                        progress = downloadStatus.percent,
                        listId = listId
                    )
                    //emit(DownloadResult.Progress(downloadStatus.percent))
                }

                is DownloadResult.Success -> {

                    resultDao.updatePdfPath(
                        path = downloadStatus.filePath,
                        listId = listId,
                        progress = 100
                    )
                    // Log.d(TAG, "Download path: ${downloadStatus.filePath}")
                    //emit(DownloadResult.Success(filePath = downloadStatus.filePath))
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

