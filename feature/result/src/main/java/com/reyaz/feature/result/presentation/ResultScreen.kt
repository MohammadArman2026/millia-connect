package com.reyaz.feature.result.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.reflect.KFunction1

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    uiState: ResultUiState,
    onEvent: KFunction1<ResultEvent, Unit>,
    onNavigateBack: () -> Boolean,
) {

}