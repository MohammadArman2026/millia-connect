package com.reyaz.feature.notice.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reyaz.feature.notice.data.local.NoticeEntity
import com.reyaz.feature.notice.data.model.NoticeType
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotice(notice: NoticeEntity)

    @Query("SELECT * FROM NoticeEntity WHERE typeId = :noticeType")
    fun observeNotices(noticeType: String): Flow<List<NoticeEntity>>

}