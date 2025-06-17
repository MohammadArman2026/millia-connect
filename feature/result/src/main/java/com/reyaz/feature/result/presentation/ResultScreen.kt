package com.reyaz.feature.result.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyaz.core.ui.components.TranslucentLoader
import com.reyaz.feature.result.presentation.components.DropDownComposable
import com.reyaz.feature.result.presentation.components.ResultCard


private const val TAG = "RESULT_SCREEN"

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    uiState: ResultUiState,
    onEvent: (ResultEvent) -> Unit = {},
    onNavigateBack: () -> Unit,
    openPdf: (String) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = modifier.padding(16.dp)/*.verticalScroll(rememberScrollState())*/,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // form
            item {
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

            // recent searches text
            if (uiState.historyList.isNotEmpty())
                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                    Text("Recent Search")
                }

            // result card
            items(uiState.historyList) {
                ResultCard(
                    courseName = it.courseName,
                    items = it.resultList,
                    onResultEvent = onEvent,
                    courseId = it.courseId,
                    openPdf = openPdf
                )
            }
        }
        if (uiState.isLoading) {
            TranslucentLoader(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

