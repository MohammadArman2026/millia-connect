package com.reyaz.feature.result.data.local.converters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date:Date?): Long? {
        return date?.time
    }
}