package com.reyaz.feature.notice.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reyaz.core.common.utils.shareTextList
import com.reyaz.core.ui.components.CustomListDivider
import com.reyaz.core.ui.components.ListItemWithTrailingIcon
import com.reyaz.core.ui.components.textWithIndicator
import com.reyaz.core.ui.helper.LinkHandler
import com.reyaz.core.ui.helper.getListItemModel
import com.reyaz.feature.notice.domain.model.TabConfig
import com.reyaz.feature.notice.presentation.components.CustomTrailingIcon
import com.reyaz.feature.notice.presentation.components.LoadingBar
import com.reyaz.feature.notice.presentation.components.NoticeTabs

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
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 1,
        initialFirstVisibleItemScrollOffset = 0
    )

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        isRefreshing = false
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Retry",
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                onEvent(
                    NoticeEvent.FetchRemoteNotice(
                        type = TabConfig.entries[uiState.selectedTabIndex].type,
                        forceRefresh = true
                    )
                )
            }
        }
    }

    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onEvent(
                NoticeEvent.FetchRemoteNotice(
                    type = TabConfig.entries[uiState.selectedTabIndex].type,
                    forceRefresh = true
                )
            )
        }
    ) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NoticeTabs(
                selectedTabIndex = uiState.selectedTabIndex,
                onTabSelected = { onEvent(NoticeEvent.OnTabClick(it)) }
            )

            AnimatedVisibility(uiState.isLoading) {
                LoadingBar()
            }

            LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                if (uiState.noticeList.isNotEmpty())
                    item {
                        Text(
                            "Long press to share",
                            modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.outline
                        )
                        CustomListDivider()
                    }
                items(uiState.noticeList) { notice ->
                    var isDownloading by remember { mutableStateOf(false) }

                    // Cancel downloading when tab switches
                    LaunchedEffect(uiState.selectedTabIndex) {
                        if (isDownloading) {
                            isDownloading = false
                        }
                    }
                    notice.title?.let { title ->
                        val actionModel = getListItemModel(
                            link = notice.link,
                            path = notice.path,
                            downloadPdf = {
                                isDownloading = true
                                notice.link?.let {
                                    onEvent(NoticeEvent.DownloadPdf(url = it, title = title))
                                }
                            },
                            deletePdf = {
                                isDownloading = false
                                notice.path?.let {
                                    onEvent(NoticeEvent.DeleteFileByPath(title = title, path = it))
                                }
                            },
                            openLink = { notice.link?.let { linkHandler.openInBrowser(it) } }
                        )

                        ListItemWithTrailingIcon(
                            textWithIndicator = textWithIndicator(title, !notice.isRead),
                            date = notice.fetchedOn,
                            trailingIcon = {
                                notice.link?.let {
                                    CustomTrailingIcon(
                                        downloadProgress = notice.progress,
                                        onIconClick = { actionModel.onClick?.invoke() },
                                        icon = actionModel.icon,
                                        isDownloading = isDownloading
                                    )
                                }
                            },
                            onClick = {
                                when {
                                    notice.path != null -> openPdf(notice.path)
                                    else -> actionModel.onClick?.invoke()
                                }
                            },
                            onLongClick = {
                                context.shareTextList(listOf("\uD83D\uDCE2 Notice: $title", "\uD83D\uDD17 Link: ${notice.link  ?: "No link found"}"))
                            }
                        )
                    }
                }
            }
        }
    }
}
