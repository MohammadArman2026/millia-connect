package com.reyaz.feature.notice.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyaz.feature.notice.domain.model.TabConfig

@Composable
 fun NoticeTabs(
    selectedTabIndex: Int,
    onTabSelected: (TabConfig) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp
    ) {
        TabConfig.entries.forEach { tab ->
            Tab(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = { Text(tab.title) },
                selected = selectedTabIndex == tab.ordinal,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}