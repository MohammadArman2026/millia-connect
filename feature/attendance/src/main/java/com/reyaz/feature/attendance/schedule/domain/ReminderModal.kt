package com.reyaz.feature.attendance.schedule.domain

import androidx.annotation.StringRes
import java.util.concurrent.TimeUnit

data class ReminderModal(
    @StringRes val durationRes: Int,
    val duration: Long,
    val unit: TimeUnit,
    val task: String
)