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
    suspend fun insertNotice(notice: NoticeEntity)

    @Query("SELECT * FROM NoticeEntity WHERE typeId = :noticeType ORDER BY createdOn ASC")
    fun observeNotices(noticeType: String): Flow<List<NoticeEntity>>

}