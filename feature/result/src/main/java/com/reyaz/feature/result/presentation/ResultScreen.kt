package com.reyaz.feature.result.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyaz.core.ui.components.TranslucentLoader
import com.reyaz.feature.result.presentation.components.CourseItemComposable
import com.reyaz.feature.result.presentation.components.ResultFormComposable
import kotlinx.coroutines.delay


private const val TAG = "RESULT_SCREEN"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    uiState: ResultUiState,
    onEvent: (ResultEvent) -> Unit = {},
    onNavigateBack: () -> Unit,
    openPdf: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    /*val scrollState = rememberLazyListState(0)

    LaunchedEffect(uiState.historyList) {
        if (uiState.historyList.isNotEmpty()) {
            scrollState.scrollToItem(1)
        }
    }*/
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            val result = snackbarHostState.showSnackbar(message = it, actionLabel = "Retry", withDismissAction = true)
            if (result == SnackbarResult.ActionPerformed) {
                onEvent(ResultEvent.Initialize)
            }
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1)
            isRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onEvent(ResultEvent.Initialize) },
        modifier = modifier
    ) {
    Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                //state = scrollState,
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // form
                item {
                    ResultFormComposable(uiState, onEvent)
                }

                // recent searches text
                if (uiState.historyList.isNotEmpty()) {
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        Text("Recent Search")

                    }

                    // course list
                    items(uiState.historyList) {
                        CourseItemComposable(
                            courseName = it.courseName,
                            resultList = it.resultList,
                            courseId = it.courseId,
                            lastSync = it.syncDate,
                            mostRecent = it.latestListDate,
                            onResultEvent = onEvent,
                            openPdf = openPdf,
                            hasNewResults = it.hasNewResult
                        )
                    }
                }
            }
            if (uiState.isLoading) {
                TranslucentLoader(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

