package com.reyaz.feature.notice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reyaz.feature.notice.data.local.dao.NoticeDao

@Database(entities = [NoticeEntity::class], version = 6, exportSchema = true)
abstract class NoticeDatabase : RoomDatabase() {
    abstract fun noticeDao(): NoticeDao

    companion object{
        const val DB_NAME = "notice_db"
    }
}