package com.reyaz.feature.attendance.schedule.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyaz.feature.attendance.schedule.data.dao.CombinedScheduleTaskModel
import com.reyaz.feature.attendance.schedule.domain.AttendanceType
import com.reyaz.feature.attendance.schedule.presentation.ScheduleUiState
import java.time.LocalTime

@Composable
fun ScheduleList(
    navigateToEditSchedule: (Int) -> Unit = {},
    uiState: ScheduleUiState,
    upsertAttendance: (Int, Int, AttendanceType) -> Unit,
    upsertTask: (Int, Int, String?, Int) -> Unit,
    onScheduleTaskReminder: (CombinedScheduleTaskModel, String?, Int) -> Unit,
    saveDailyClassReminder: (Boolean, LocalTime, CombinedScheduleTaskModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
//        Log.d("LIST", scheduleUiState.scheduleList.toString() ) // correct list

        items(items = uiState.scheduleList) { scheduleModel ->
//            Log.d("CURRENT_TASK", "subject name: ${scheduleModel.subject} \ntask: ${scheduleModel.task}")
            ScheduleItem(
                scheduleObj = scheduleModel,
                onEditClick = { navigateToEditSchedule(scheduleModel.scheduleId) },
                onAttendanceClick = { attendanceType ->
                    upsertAttendance(scheduleModel.scheduleId, scheduleModel.subjectId, attendanceType)
                },
                repeatReminderSwitchAction = { subName, repeat ->
//                        viewModel.modifySubjectInRepeatReminderList(
//                            subName,
//                            repeat,
//                        )
//                    onScheduleReminder()
                },
                upsertTask = {task, remindBefore->
                    upsertTask(scheduleModel.scheduleId, scheduleModel.subjectId, task, remindBefore)
                    onScheduleTaskReminder(scheduleModel.copy(timestamp = uiState.timestamp), task, remindBefore)
                },
                saveDailyClassReminder = saveDailyClassReminder
            )
        }
    }
}