package com.reyaz.feature.attendance.schedule.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.attendance.schedule.presentation.ScheduleUiState

@Composable
fun CalendarComposable(
    uiState: ScheduleUiState,
    listState: LazyListState,
    onDateClick: (Int) -> Unit
) {
    // month
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.DateRange, contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "${uiState.selectedMonth}, ${uiState.selectedYear}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
    //day date
    DayDate(
        listState,
        uiState,
        onDateSelect = onDateClick
    )
}

@Composable
private fun DayDate(
    listState: LazyListState,
    uiState: ScheduleUiState,
    onDateSelect: (Int) -> Unit
) {
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        val list = uiState.dayDateList
        items(list.size) { index ->
            DayDateLayout(
                onDateClick = { onDateSelect(index) },
//                        scheduleViewModel.selectDateIndex(index)
                day = list[index].day,
                date = list[index].date,
                currentDate = index == 15,
                isSelected = index == uiState.selectedDateIndex,
            )
        }
    }
}