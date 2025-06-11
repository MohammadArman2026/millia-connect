package com.reyaz.feature.attendance.schedule.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class AttendanceType(val icon: ImageVector, val title: String, val tint: Color?) {
    Present(
        title = "Present",
        icon = Icons.Default.CheckCircle,
        tint = Color(0xFF00BB00)
    ),
    Absent(
        title = "Absent",
        icon = Icons.Default.RemoveCircle,
        tint = Color(0xFFFF3333)
    ),
    Cancelled(
        title = "Cancelled",
        icon = Icons.Default.Warning,
        tint = Color(0xFFFFBE3B)
    ),
    MarkAttendance(
        title = "Attendance",
        icon = Icons.Default.AddTask,
        tint = null
    ),
}











