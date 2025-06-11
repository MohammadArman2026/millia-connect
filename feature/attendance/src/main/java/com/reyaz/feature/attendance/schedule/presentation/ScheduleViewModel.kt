package com.reyaz.feature.attendance.schedule.presentation

import com.reyaz.feature.attendance.schedule.data.dao.CombinedScheduleTaskModel
import com.reyaz.feature.attendance.schedule.domain.AttendanceType
import com.reyaz.feature.attendance.schedule.domain.models.DayDateModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ScheduleViewModel(
//    private val scheduleRepository: ScheduleRepository,
//    private val workManagerTaskRepository: TaskReminderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState

    private val todayDateIndex: Int = 15
    private val _calendar = Calendar.getInstance()

    init {
        getDayDateList()
        onDateClick(_uiState.value.selectedDateIndex)
    }

    private fun getDayDateList() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val dayDateList = mutableListOf<DayDateModel>()
            calendar.add(Calendar.DAY_OF_YEAR, -todayDateIndex) // Start 15 days before current day

            repeat(60) {
                val day =
                    calendar.getDisplayName(
                        Calendar.DAY_OF_WEEK,
                        Calendar.SHORT,
                        Locale.getDefault()
                    )
                        ?: ""
                val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
                dayDateList.add(DayDateModel(day = day, date = date))
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            _uiState.update {
                it.copy(dayDateList = dayDateList)
            }
        }
    }

    //    private var timestamp: Int = 0      // TODO: check its value on different date click
    fun onDateClick(selectedIndex: Int) {
        // Launch a coroutine to update state in background
        viewModelScope.launch {
            // getting the timestamp according to selected index
            val selectedDateCalendar = Calendar.getInstance()
            selectedDateCalendar.set(Calendar.DAY_OF_MONTH, _calendar.get(Calendar.DAY_OF_MONTH))
            selectedDateCalendar.set(Calendar.YEAR, _calendar.get(Calendar.YEAR))
            selectedDateCalendar.add(Calendar.DAY_OF_YEAR, selectedIndex - todayDateIndex)


            //updating month on the basis of selected date
            val selectedMonth = selectedDateCalendar.getDisplayName(
                Calendar.MONTH, Calendar.SHORT, Locale.getDefault()
            ) ?: ""
            val selectedYear = selectedDateCalendar.get(Calendar.YEAR) % 100
            val selectedDay = selectedDateCalendar.getDisplayName(
                Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()
            ) ?: ""
            val timestamp = getTimestampForSelectedDay(selectedDateCalendar.time)
//            val scheduleList = scheduleRepository.getScheduleAndTaskList(selectedDay, timestamp)
//            Log.d("SCHEDULE_VM", scheduleList.toString())
            _uiState.update {
                it.copy(
                    scheduleList = /*scheduleList*/emptyList(),
                    selectedMonth = selectedMonth,
                    selectedYear = selectedYear,
                    selectedDateIndex = selectedIndex,
                    selectedDay = selectedDay,
                    timestamp = timestamp
                )
            }

        }
    }

    private fun getTimestampForSelectedDay(selectedDate: Date): Int {
        val formatter = SimpleDateFormat("yyMMdd", Locale.getDefault()) // format for YYMMDD
        val formattedDate = formatter.format(selectedDate)
        return formattedDate.toInt() // convert formatted string to Long
    }

    fun upsertAttendance(subjectId: Int, attendance: AttendanceType, scheduleId: Int) {
        viewModelScope.launch {
            /*scheduleRepository.upsertAttendance(
                attendance = attendance,
                subjectId = subjectId,
                timeStamp = _uiState.value.timestamp,
                scheduleId = scheduleId
            )*/
        }
    }

    suspend fun upsertTask(
        scheduleId: Int,
        subjectId: Int,
        task: String?,
        taskReminderBefore: Int
    ) {
        /*scheduleRepository.upsertTask(
            task = task,
            subjectId = subjectId,
            timeStamp = _uiState.value.timestamp,
            scheduleId = scheduleId,
            taskReminderBefore = taskReminderBefore
        )*/
    }

    fun scheduleTaskReminder(
        task: String = "This is dummy task",
        remindBefore: Int = 5,
        unit: TimeUnit = TimeUnit.SECONDS,       //TODO: change to hour
        scheduleObj: CombinedScheduleTaskModel
    ) {
        /*val reminderKey = NotificationConstant.getTaskReminderKey(
            scheduleId = scheduleObj.scheduleId,
            subjectId = scheduleObj.subjectId,
            timeStamp = scheduleObj.timestamp ?: 0
        )
        if (remindBefore != 0) {
            workManagerTaskRepository.scheduleTaskReminder(
                task,
                remindBefore.toLong(),
                unit,
                taskKey = reminderKey,
                scheduleObj
            )
        } else {
            // cancel reminder
            Log.d("CANCEL_REMINDER", "cancel reminder")
            cancelReminder(reminderKey)
        }*/
    }

    suspend fun scheduleDailyClassReminder(
        time: LocalTime,
        cancelReminder: Boolean,
        scheduleObj: CombinedScheduleTaskModel
    ) {
       /* viewModelScope.launch {
            val reminderKey = NotificationConstant.getDailyClassReminderKey(
                scheduleId = scheduleObj.scheduleId
            )
            if (cancelReminder) {
                // cancel reminder
                cancelReminder(reminderKey)
            } else {
                workManagerTaskRepository.scheduleDailyClassReminder(
                    scheduleObj = scheduleObj,
                    time = time
                )
            }
            scheduleRepository.saveDailyClassReminder(
                scheduleId = scheduleObj.scheduleId,
                time = if (cancelReminder) null else time
            )
        }*/
    }

    private fun cancelReminder(
        reminderKey: String
    ) {
        //workManagerTaskRepository.cancelScheduledWork(reminderKey)
    }
}