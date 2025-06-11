package com.reyaz.feature.attendance.schedule.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reyaz.feature.attendance.schedule.domain.AttendanceType
import java.time.LocalTime

@Entity(tableName = "subject_table")
data class SubjectModel(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int = 0, // Foreign key referencing ScheduleModel
    val subject: String
)

data class CombinedScheduleTaskModel(
    val scheduleId: Int,
    val subjectId: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val day: String,
    val timestamp: Int? = null,
    val subject: String? = null,
    val attendance: AttendanceType? = AttendanceType.MarkAttendance,
    var task: String? = null,
    val dailyReminderTime: LocalTime? = null,
    val classReminderTime: LocalTime? = null,
    val taskReminderBefore: Int = 0
)
