package com.reyaz.feature.notice.data

import android.util.Log
import com.reyaz.core.common.utlis.toTimeAgoString
import com.reyaz.core.network.PdfManager
import com.reyaz.core.network.model.DownloadResult
import com.reyaz.feature.notice.data.local.NoticeEntity
import com.reyaz.feature.notice.data.local.dao.NoticeDao
import com.reyaz.feature.notice.data.model.NoticeDto
import com.reyaz.feature.notice.data.model.NoticeType
import com.reyaz.feature.notice.data.remote.NoticeScraper
import com.reyaz.feature.notice.domain.model.Notice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private const val TAG = "NOTICE_REPOSITORY"

class NoticeRepository(
    private val scraper: NoticeScraper,
    private val noticeDao: NoticeDao,
    private val pdfManager: PdfManager,
    ) {

    fun observeNotice(noticeType: NoticeType): Flow<List<Notice>> = noticeDao.observeNotices(noticeType.typeId).map { noticeEntities -> noticeEntities.map { noticeEntity -> noticeEntity.entityToDomain() } }

    suspend fun refreshNotice(noticeType: NoticeType): Result<Unit> {
        return try {
            val noticeResult = scraper.scrapNotices(noticeType)
            if (noticeResult.isSuccess) {
                Log.d(TAG, noticeResult.getOrNull().toString())
                noticeResult.getOrThrow().map {
                    noticeDao.insertNotice(it.toNoticeEntity())
                }
                Result.success(Unit)
            } else {
                throw noticeResult.exceptionOrNull() ?: Exception("Error while refreshing notice")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error: $e")
            Result.failure(e)
        }
    }

    fun downloadPdf(
        url: String,
        fileName: String
    ): Flow<DownloadResult> = flow {
         Log.d(TAG, "Download url: $url")
        pdfManager.downloadPdf(url = url, fileName = fileName).collect { downloadStatus ->
             Log.d(TAG, "Download status: $downloadStatus")
            when (downloadStatus) {
                is DownloadResult.Error -> {
                    // Log.d(TAG, "Download error: ${downloadStatus.exception}")
                    noticeDao.updatePdfPath(
                        path = null,
                        fileName = fileName,
                        progress = null
                    )
                    emit(DownloadResult.Error(downloadStatus.exception))
                }

                is DownloadResult.Progress -> {
                    // Log.d(TAG, "Download progress: ${downloadStatus.percent}")
                    noticeDao.updateDownloadProgress(
                        progress = downloadStatus.percent,
                        filename = fileName
                    )
                    emit(DownloadResult.Progress(downloadStatus.percent))
                }

                is DownloadResult.Success -> {

                    noticeDao.updatePdfPath(
                        path = downloadStatus.filePath,
                        fileName = fileName,
                        progress = 100
                    )
                    // Log.d(TAG, "Download path: ${downloadStatus.filePath}")
                    emit(DownloadResult.Success(filePath = downloadStatus.filePath))
                }
            }
        }
    }

     suspend fun deleteFileByPath(path: String, filename: String) {
        pdfManager.deleteFile(path)
        noticeDao.updatePdfPath(path = null, fileName = filename, progress = null)
        // Log.d(TAG, "path deleted from room")
    }

    suspend fun markNoticesAsRead(id: String) {
        noticeDao.markNoticeAsRead(id)
    }
}

fun NoticeEntity.entityToDomain() = Notice(title = title, link = link, path = path, progress = progress, isRead = isViewed, fetchedOn = createdOn.toTimeAgoString())

fun NoticeDto.toNoticeEntity(): NoticeEntity {
    return NoticeEntity(
        typeId = type.typeId,
        title = title,
        link = url,
        createdOn = createdOn,
        isViewed = false
    )
}
