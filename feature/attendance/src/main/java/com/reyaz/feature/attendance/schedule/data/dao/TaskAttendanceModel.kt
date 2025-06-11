package com.reyaz.feature.attendance.schedule.data.dao

import androidx.room.Entity
import com.reyaz.feature.attendance.schedule.domain.AttendanceType
import java.time.LocalTime

@Entity(
    tableName = "taskAttendance",
    primaryKeys = ["scheduleId", "subjectId", "timestamp"]
)
data class TaskAttendanceModel(
    val scheduleId: Int,    // Foreign key referencing ScheduleModel
    val subjectId: Int,
    val timestamp: Int, // Timestamp to track the date
    var attendance: AttendanceType? = null,  // Task or note for the class on the specific date
    var task: String? = null,  // Task or note for the class on the specific date
    val taskReminderBefore: Int = 0,
    val classReminderTime: LocalTime?=null,
)