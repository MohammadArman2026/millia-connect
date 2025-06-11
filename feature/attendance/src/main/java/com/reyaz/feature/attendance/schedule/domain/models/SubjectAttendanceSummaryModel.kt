package com.reyaz.feature.attendance.schedule.domain.models

data class SubjectAttendanceSummaryModel(
    val subject: String,
    val totalClasses: Int,
    val attendedClasses: Int,
    val absentClasses: Int,
    val percentage: Float? = 89f,    //
)