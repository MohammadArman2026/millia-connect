package com.reyaz.feature.attendance.schedule.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.attendance.schedule.data.dao.CombinedScheduleTaskModel
import com.reyaz.feature.attendance.schedule.domain.AttendanceType
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleItem(
    scheduleObj: CombinedScheduleTaskModel,
    modifier: Modifier = Modifier,
    isBubbleFilled: Boolean = false,
    onReminderClick: () -> Unit = {},
    onEditClick: () -> Unit,
    onAttendanceClick: (AttendanceType) -> Unit,
    repeatReminderSwitchAction: (subject: String, repeat: Boolean) -> Unit,
    upsertTask: (String?, Int) -> Unit,
    saveDailyClassReminder: (Boolean, LocalTime, CombinedScheduleTaskModel) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var showReminderDialog by rememberSaveable { mutableStateOf(false) }
    var remindBeforeHour by remember { mutableStateOf(scheduleObj.dailyReminderTime) }
    var isScheduleReminder by remember { mutableStateOf(scheduleObj.dailyReminderTime != null || scheduleObj.classReminderTime != null) }

    LaunchedEffect(scheduleObj.dailyReminderTime, scheduleObj.classReminderTime) {
        isScheduleReminder = scheduleObj.dailyReminderTime != null || scheduleObj.classReminderTime != null
    }
/*
    Log.d(
        "SCHEDULE_ITEM",
        "dailyReminderObj = ${scheduleObj.subject}: ${scheduleObj.dailyReminderTime}"
    )
    Log.d(
        "SCHEDULE_ITEM",
        "classReminder = ${scheduleObj.subject}: ${scheduleObj.classReminderTime}"
    )
    Log.d("SCHEDULE_ITEM", "isSheduleReminder = ${isScheduleReminder}")*/

    var obj by remember { mutableStateOf(scheduleObj) }
//    Log.d("ATTENDANCE_OBJ", "ScheduleObj Attendance: ${scheduleObj.attendance} date: ${(scheduleObj.timestamp)?.rem(100)}")
//    Log.d("ATTENDANCE_Muta", "Local Attendance: ${scheduleObj.attendance} date: ${(obj.timestamp)?.rem(100)}")

    LaunchedEffect(scheduleObj.attendance) {
//        Log.d("ATTENDANCE_OBJ_UPDATE", "Updated scheduleObj: ${scheduleObj.attendance} date: ${(scheduleObj.timestamp)?.rem(100)}")
        obj = scheduleObj
    }


    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        //time
        Column(
            horizontalAlignment = Alignment.End
        ) {
            //time
            Text(
                text = scheduleObj.startTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                fontSize = 14.sp
            )
            Text(
                text = scheduleObj.endTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                fontSize = 10.sp
            )
        }
        //bubble and vertical line
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.padding(start = 8.dp, end = 16.dp),
        ) {
            //line (timeline)
            Box(
                modifier = Modifier
//                .padding(start = 8.dp)
                    .width(1.dp)
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
            //bubble
            Box(
                modifier = if (isBubbleFilled) {
                    Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(50))
                        .border(
                            2.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(50)
                        )
                        .background(
                            MaterialTheme.colorScheme.error
                        )
                } else {
                    Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(50))
                        .border(
                            2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50)
                        )
                        .background(

                            MaterialTheme.colorScheme.background

                        )
                }
            )
        }
        // subject box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            // subject headline row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //reminder
                IconButton(onClick = {
                    showReminderDialog = true
                }) {
                    Icon(
                        imageVector = if (isScheduleReminder) Icons.Default.Notifications else Icons.Outlined.Notifications,
                        tint = if (isScheduleReminder) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "reminder",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = "${scheduleObj.subject}",
                    fontWeight = FontWeight.Bold, fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        modifier = Modifier.size(20.dp)
                    )

                }
            }

            //task and attendance
            TaskAttendance(scheduleObj, onAttendanceClick, upsertTask = upsertTask)
        }
    }
    // Reminder Dialog
    if (showReminderDialog) {
        var repeatSwitchOn by rememberSaveable {
            mutableStateOf(isScheduleReminder)
        }
        var reminderTime by remember { mutableStateOf(scheduleObj.dailyReminderTime?: LocalTime.now()) }
        AlertDialog(
            onDismissRequest = { showReminderDialog = false },
            title = { Text("Class Reminder") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var isDaily by rememberSaveable { mutableStateOf(true) }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Switch(
                            checked = repeatSwitchOn,
                            onCheckedChange = {
                                repeatSwitchOn = it
//                                repeatReminderSwitchAction( it)
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isDaily,
                                onClick = { isDaily = true },
                                enabled = repeatSwitchOn
                            )
                            Text("Daily")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = !isDaily,
                                onClick = { isDaily = false },
                                enabled = /*repeatSwitchOn*/false

                            )
                            Text("Once", color = MaterialTheme.colorScheme.outline)
                        }

                    }
//                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier,
//                            horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimePicker(
                            modifier = Modifier.weight(1f),
                            value = reminderTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                            label = "A day before, at:",
                            onTimeSelect = { localTime ->
                                reminderTime = localTime
                            },
                            isEnabled = repeatSwitchOn
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = true,
                    onClick = {
                        showReminderDialog = false
                        isScheduleReminder = repeatSwitchOn
                        saveDailyClassReminder(!repeatSwitchOn, reminderTime, scheduleObj)
                    }) {
                    Text("Save")
                }
            }
        )
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewScheduleItem() {

    /*ScheduleItem(
        scheduleObj = CombinedScheduleTaskModel(
            scheduleId = 1,
            subjectId = 101,
            startTime = LocalTime.of(9, 0),  // 9:00 AM
            endTime = LocalTime.of(10, 0),   // 10:00 AM
            day = "Monday",
            dailyReminder = true,
            timestamp = 1234567890,
            subject = "Mathematics",
            attendance = AttendanceType.MarkAttendance,
            task = "Complete Chapter 5",
            taskReminder = true
        ),
        isBubbleFilled = true,
        onReminderClick = { *//* Handle reminder click *//* },
        onEditClick = { *//* Handle edit click *//* },
        onAttendanceClick = { *//* Handle attendance click *//* },
        repeatReminderSwitchAction = { subject, repeat -> *//* Handle reminder switch action *//* },
        remin = true,
        upsertTask = { *//* Handle upsert task *//* }
    )*/
}