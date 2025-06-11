package com.reyaz.feature.attendance.schedule.data.dao.utils

import androidx.room.TypeConverter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? {
        return time?.format(formatter)
    }

    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? {
        return timeString?.let {
            LocalTime.parse(it, formatter)
        }
    }
}