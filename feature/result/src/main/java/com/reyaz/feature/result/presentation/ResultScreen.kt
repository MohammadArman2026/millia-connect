package com.reyaz.feature.result.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyaz.feature.result.presentation.components.AutoCompleteDropDown

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    uiState: ResultUiState,
    onEvent: (ResultEvent) -> Unit = {},
    onNavigateBack: () -> Unit,
) {
    Column(
        modifier = modifier.padding(16.dp)/*.verticalScroll(rememberScrollState())*/,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
       /* DropDownWithLoader(
            modifier = modifier,
            isLoading = uiState.typeLoading,
            selectedText = uiState.selectedType,
            items = uiState.courseTypeList.map { it.name },
            onItemSelected = { onEvent(ResultEvent.UpdateType(it)) }
        )

        DropDownComposable(
            modifier = modifier,
            options = uiState.courseTypeList.map { it.name },
            label = "Course",
            onOptionSelected = { onEvent(ResultEvent.UpdateCourse(it)) }
        )

        DropdownInsteaComposable(
            modifier = modifier,
            options = uiState.courseTypeList.map { it.name },
            label = "Course",
            onOptionSelected = { onEvent(ResultEvent.UpdateCourse(it)) },
//            leadingIcon = TODO(),
//            isError = TODO(),
//            errorMessage = TODO(),
//            selectedOption = TODO(),
//            keyboardActions = TODO(),
//            onAddItemClicked = TODO(),
//            isEnabled = TODO(),
            isLoadingOption = false,
//            isExpandable = TODO(),
//            showAddBtn = TODO()
        )

        DropdownButtonComposable(
            options = uiState.courseTypeList.map { it.name }
        )
*/
        AutoCompleteDropDown(
            isLoading = uiState.typeLoading,
            selectedText = uiState.selectedType,
            list = uiState.courseTypeList.map { it.name },
            onItemSelected = { onEvent(ResultEvent.UpdateType(it)) },
            label = "Course Type"
        )
    }


//    SearchBar() { }

//    ExposedDropdownMenuBox() { }
}