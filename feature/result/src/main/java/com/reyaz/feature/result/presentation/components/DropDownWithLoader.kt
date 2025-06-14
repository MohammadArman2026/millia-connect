package com.reyaz.feature.result.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DropDownWithLoader(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    selectedText: String = "",
    items: List<String> = emptyList(),
    onItemSelected: (String) -> Unit,
) {

}