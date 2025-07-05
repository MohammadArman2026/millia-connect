package com.reyaz.feature.result.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyaz.feature.result.presentation.ResultEvent
import com.reyaz.feature.result.presentation.ResultUiState

@Composable
fun ResultFormComposable(
    uiState: ResultUiState,
    onEvent: (ResultEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DropDownComposable(
            options = uiState.courseTypeList.map { it.name },
            label = "Course Type",
            onOptionSelected = {
                onEvent(ResultEvent.UpdateType(it))
                onEvent(ResultEvent.LoadCourse)
            },
            isLoading = uiState.typeLoading,
            enabled = uiState.courseTypeEnabled,
            value = uiState.selectedType
        )
        DropDownComposable(
            options = uiState.courseNameList.map { it.name },
            label = "Course",
            onOptionSelected = { onEvent(ResultEvent.UpdateCourse(it)) },
            isLoading = uiState.courseLoading,
            enabled = uiState.courseEnabled,
            value = uiState.selectedCourse
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(ResultEvent.LoadResult) },
            enabled = uiState.btnEnabled
        ) {
            Text(text = "Track Result", fontWeight = FontWeight.Bold)
        }
    }
}