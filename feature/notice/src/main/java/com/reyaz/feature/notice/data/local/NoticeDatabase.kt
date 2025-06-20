package com.reyaz.feature.notice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reyaz.feature.notice.data.local.dao.NoticeDao
import com.reyaz.feature.notice.data.local.dao.NoticeTypeDao

@Database(entities = [NoticeEntity::class, NoticeTypeEntity::class], version = 1, exportSchema = true)
abstract class NoticeDatabase : RoomDatabase() {
    abstract fun noticeDao(): NoticeDao
    abstract fun noticeTypeDao(): NoticeTypeDao

    companion object{
        const val DB_NAME = "notice_db"
    }
}