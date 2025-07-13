package com.reyaz.feature.notice.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reyaz.feature.notice.data.local.NoticeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(notice: List<NoticeEntity>)

    @Query("SELECT * FROM NoticeEntity WHERE typeId = :noticeType ORDER BY createdOn DESC")
    fun observeNotices(noticeType: String): Flow<List<NoticeEntity>>

    @Query("UPDATE NoticeEntity SET path = :path, progress = :progress WHERE title = :fileName")
    suspend fun updatePdfPath(path: String?, fileName: String, progress: Int? = null)

    @Query("UPDATE NoticeEntity SET isViewed = 1 WHERE typeId = :id")
    suspend fun markNoticeAsRead(id: String)

    @Query("UPDATE NoticeEntity SET progress = :progress WHERE title = :filename")
    suspend fun updateDownloadProgress(progress: Int?, filename: String)


}