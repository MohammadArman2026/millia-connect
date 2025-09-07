package com.reyaz.feature.result.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.reyaz.feature.result.data.local.converters.DateConverter
import com.reyaz.feature.result.data.local.dao.ResultDao
import com.reyaz.feature.result.data.local.entities.CourseEntity
import com.reyaz.feature.result.data.local.entities.ResultListEntity


@Database(
    entities = [CourseEntity::class, ResultListEntity::class],
    exportSchema = true,
    version = 11,
)
@TypeConverters(
    DateConverter::class
)
abstract class ResultDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao

    companion object {
        const val DB_NAME = "result_db"
    }
}