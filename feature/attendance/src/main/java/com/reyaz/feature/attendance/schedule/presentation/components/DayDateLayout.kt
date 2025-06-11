package com.reyaz.feature.attendance.schedule.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DayDateLayout(
    onDateClick: () -> Unit,
    day: String,
    date: String,
    currentDate: Boolean = false,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val dateModifier = Modifier
        .clip(CircleShape)
        .then(
            if (isSelected) {
                Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(4.dp)
            } else if (currentDate) {
                Modifier
                    .border(
                        2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .padding(4.dp)
            } else {
                Modifier.padding(4.dp)
            }
        )

    Column(
        modifier = modifier.clickable { onDateClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //day
        Text(
            text = day, fontWeight = FontWeight.Bold,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )

        //date
        Box(
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Text(
                text = date,
                modifier = dateModifier,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isSelected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            )
        }
    }
}