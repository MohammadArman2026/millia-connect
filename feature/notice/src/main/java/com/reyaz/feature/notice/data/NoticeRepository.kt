package com.reyaz.feature.notice.data

import android.util.Log
import com.reyaz.feature.notice.data.local.NoticeEntity
import com.reyaz.feature.notice.data.local.dao.NoticeDao
import com.reyaz.feature.notice.data.model.NoticeDto
import com.reyaz.feature.notice.data.model.NoticeType
import com.reyaz.feature.notice.data.remote.NoticeScraper
import com.reyaz.feature.notice.domain.model.Notice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TAG = "NOTICE_REPOSITORY"

class NoticeRepository(
    private val scraper: NoticeScraper,
    private val noticeDao: NoticeDao,
) {

    fun observeNotice(noticeType: NoticeType): Flow<List<Notice>> {
//        val localList: Flow<List<Notice>> = noticeDao.observeNotices(noticeType.typeId)
//            .map { it.sortedByDescending { noticeList -> noticeList.createdOn }
//                .map { noticeEntity -> noticeEntity.entityToDomain() }
//        }
//        return localList
        return noticeDao.observeNotices(noticeType.typeId).map { noticeEntities -> noticeEntities.map { noticeEntity -> noticeEntity.entityToDomain() } }
    }

    suspend fun refreshNotice(noticeType: NoticeType): Result<Unit> {
        return try {
            val noticeResult = scraper.scrapNotices(noticeType)
            if (noticeResult.isSuccess) {
                noticeResult.getOrThrow().map { noticeDao.insertNotice(it.toNoticeEntity()) }
                Result.success(Unit)
            } else {
                throw Exception("Error while refreshing notice")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error: $e")
            Result.failure(Exception("Error while refreshing notice"))
        }
    }
}

fun NoticeEntity.entityToDomain() = Notice(title = title, link = link, path = path)

fun NoticeDto.toNoticeEntity(): NoticeEntity {
    return NoticeEntity(
        typeId = type.typeId,
        title = title,
        link = url,
        createdOn = createdOn,
    )
}
