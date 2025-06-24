package com.reyaz.feature.notice.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.reyaz.core.ui.components.ListItemWithTrailingIcon
import com.reyaz.core.ui.helper.LinkHandler
import com.reyaz.core.ui.helper.getListItemModel
import com.reyaz.feature.notice.domain.model.Tabs
import com.reyaz.feature.notice.presentation.components.CustomTrailingIcon
import kotlinx.coroutines.delay

private const val TAG = "NOTICE_SCREEN"

@Composable
fun NoticeScreen(
    modifier: Modifier = Modifier,
    uiState: NoticeUiState,
    onEvent: (NoticeEvent) -> Unit,
    openPdf: (String) -> Unit,
) {
    val context = LocalContext.current
    val linkHandler = remember { LinkHandler(context) }

    var showStatus by remember { mutableStateOf(true) }

    /*var prog by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while(prog <= 100){
            prog++;
            delay(1000)
        }
    }*/

    LaunchedEffect(uiState.isLoading, uiState.errorMessage) {
        showStatus = true
        delay(200)
        if (uiState.noticeList.isNotEmpty())
            showStatus = false
    }

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = uiState.selectedTabIndex,
            edgePadding = 0.dp
        ) {
            Tabs.entries.forEach { it ->
                Tab(
                    text = {
                        Text(
                            text = it.title,
                            color = if (uiState.selectedTabIndex == it.ordinal) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                    },
                    selected = uiState.selectedTabIndex == it.ordinal,
                    onClick = { onEvent(NoticeEvent.OnTabClick(it)) }
                )
            }
        }
        AnimatedVisibility(showStatus) {
            if (uiState.isLoading || uiState.errorMessage != null)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = if (uiState.isLoading) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isLoading)
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
                    else
                        Text("${uiState.errorMessage}")
                }
        }
        LazyColumn(modifier = Modifier) {
            items(uiState.noticeList) { notice ->
//                val notice = notice1.copy(progress = prog)
                notice.title?.let { it ->
                    val actionModel = getListItemModel(
                        link = notice.link,
                        path = notice.path,
                        downloadPdf = {
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
                        listTitle = it,
                        date = notice.fetchedOn,
                        trailingIcon = {
                            notice.link?.let {
                                CustomTrailingIcon(
                                    downloadProgress = notice.progress ?: 0,
//                                    downloadProgress = prog,
                                    onIconClick = { actionModel.onClick?.let { it() } },
                                    icon = actionModel.icon
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
                        isNewItem = !notice.isRead
                    )
                }
            }
        }
    }
}

