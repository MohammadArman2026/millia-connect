package com.reyaz.feature.result.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reyaz.core.ui.theme.MilliaConnectTheme
import com.reyaz.feature.result.presentation.components.DropDownComposable

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    uiState: ResultUiState,
    onEvent: (ResultEvent) -> Unit = {},
    onNavigateBack: () -> Unit,
) {
    Column(
        modifier = modifier.padding(16.dp)/*.verticalScroll(rememberScrollState())*/,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropDownComposable(
            options = uiState.courseTypeList.map { it.name },
            label = "Course Type",
            onOptionSelected = {
                onEvent(ResultEvent.UpdateType(it))
                onEvent(ResultEvent.LoadCourse) },
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
            onClick = {onEvent(ResultEvent.LoadResult)}
        ) {
            Text(text = "Track Result")
        }

    }


//    SearchBar() { }

//    ExposedDropdownMenuBox() { }
}

/*
@Preview(showSystemUi = true)
@Composable
fun ResultScreenPreview() {
    MilliaConnectTheme(darkTheme = false) {
        ResultScreen(
            uiState = ResultUiState(),
            onNavigateBack = {}
        )
    }
}*/
