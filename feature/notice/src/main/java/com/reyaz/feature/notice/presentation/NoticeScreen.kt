package com.reyaz.feature.notice.presentation

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.reyaz.feature.notice.domain.model.Tabs
import androidx.core.net.toUri
@Composable
fun NoticeScreen(
    modifier: Modifier = Modifier,
    uiState: NoticeUiState,
    onEvent: (NoticeEvent) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val mContext = LocalContext.current

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp
        ) {
            Tabs.entries.forEach { it ->
                val index = it.ordinal
                Tab(
                    text = {
                        Text(
                            text = it.title,
                            color = if (selectedTabIndex == index) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        onEvent(NoticeEvent.ObserveNotice(it.type))
                    }
                )
            }
        }
        AnimatedVisibility(uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary)
                    Text("Loading...")
                }
            }
            /*} else if (uiState.error != null) {
                Text("Error: ${uiState.error}")*/
        }
        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(uiState.noticeList) { notice ->
                notice.title?.let {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, notice.link?.toUri())
                                startActivity(mContext, intent, null)
                            },
                        text = it
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                }
            }
        }
    }

}