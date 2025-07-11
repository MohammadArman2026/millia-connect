package com.reyaz.feature.notice.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.components.ListItemWithTrailingIcon
import com.reyaz.core.ui.components.textWithIndicator
import com.reyaz.core.ui.helper.LinkHandler
import com.reyaz.core.ui.helper.getListItemModel
import com.reyaz.core.ui.theme.MilliaConnectTheme
import com.reyaz.feature.notice.domain.model.TabConfig
import com.reyaz.feature.notice.presentation.components.CustomTrailingIcon

private const val TAG = "NOTICE_SCREEN"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeScreen(
    modifier: Modifier = Modifier,
    uiState: NoticeUiState,
    onEvent: (NoticeEvent) -> Unit,
    openPdf: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val linkHandler = remember { LinkHandler(context) }

    var showLoadingBar by remember { mutableStateOf(false) }
    var showErrorBar by remember { mutableStateOf(false) }

    /*var prog by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while(prog <= 100){
            prog++;
            delay(1000)
        }
    }*/

    LaunchedEffect(uiState.isLoading) {
        showLoadingBar = uiState.isLoading
    }

    LaunchedEffect(uiState.errorMessage) {
        if (!uiState.errorMessage.isNullOrEmpty()) {
            val result = snackbarHostState.showSnackbar(
                message = uiState.errorMessage,
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                onEvent(
                    NoticeEvent.RefreshNotice(
                        type = TabConfig.entries[uiState.selectedTabIndex].type,
                        forceRefresh = true
                    )
                )
            }
        }
    }

    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize(),
        isRefreshing = uiState.isRefreshing,
        onRefresh = {
            onEvent(
                NoticeEvent.RefreshNotice(
                    type = TabConfig.entries[uiState.selectedTabIndex].type,
                    forceRefresh = true
                )
            )
        },
    ) {
        Column(
            modifier = modifier.background(MaterialTheme.colorScheme.background)
        ) {
            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = uiState.selectedTabIndex,
                edgePadding = 0.dp
            ) {
                TabConfig.entries.forEach { it ->
                    Tab(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = it.title,
                                    color = if (uiState.selectedTabIndex == it.ordinal) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground
                                    }
                                )
                            }
                        },
                        selected = uiState.selectedTabIndex == it.ordinal,
                        onClick = { onEvent(NoticeEvent.OnTabClick(it)) }
                    )
                }
            }
            AnimatedVisibility(showLoadingBar) {
                LoadingErrorBar(
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text("Loading...")
                    }
                }
            }
            AnimatedVisibility(showErrorBar) {
                uiState.errorMessage?.let {
                    LoadingErrorBar(
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(it)
                    }
                }
            }
            LazyColumn(modifier = Modifier) {
                if (uiState.noticeList.isEmpty() && !uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxHeight() // ensures swipe gesture is captured
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No notices found.\nPull down to refresh.",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                } else
                    items(uiState.noticeList) { notice ->
                        var isDownloading by remember { mutableStateOf(false) }

                        notice.title?.let { it ->
                            val actionModel = getListItemModel(
                                link = notice.link,
                                path = notice.path,
                                downloadPdf = {
                                    isDownloading = true
                                    notice.link?.let {
                                        onEvent(
                                            NoticeEvent.DownloadPdf(
                                                url = it,
                                                title = notice.title
                                            )
                                        )
                                    }
                                },
                                deletePdf = {
                                    isDownloading = false
                                    notice.path?.let {
                                        onEvent(
                                            NoticeEvent.DeleteFileByPath(
                                                title = notice.title,
                                                path = it
                                            )
                                        )
                                    }
                                },
                                openLink = { notice.link?.let { linkHandler.openInBrowser(it) } },
                            )
                            ListItemWithTrailingIcon(
                                textWithIndicator = textWithIndicator(it, !notice.isRead),
                                date = notice.fetchedOn,
                                trailingIcon = {
                                    notice.link?.let {
                                        CustomTrailingIcon(
                                            downloadProgress = notice.progress,
//                                    downloadProgress = prog,
                                            onIconClick = { actionModel.onClick?.let { it() } },
                                            icon = actionModel.icon,
                                            isDownloading = isDownloading
                                        )
                                    }
                                },
                                onClick = {
                                    when {
                                        notice.path != null -> {
                                            openPdf(notice.path)
                                        }

                                        else -> actionModel.onClick?.let { it() }
                                    }
                                },
                            )
                        }
                    }
            }
        }
    }
}

@Composable
fun LoadingErrorBar(color: Color, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}


@Preview(showBackground = true)
@Composable
fun TabPreview() {
    MilliaConnectTheme {
        Tab(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Notice",
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(16.dp)
                            .background(MaterialTheme.colorScheme.errorContainer),
                    ) {
                        Text(
                            text = "6",
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            selected = false,
            onClick = { }
        )
    }
}