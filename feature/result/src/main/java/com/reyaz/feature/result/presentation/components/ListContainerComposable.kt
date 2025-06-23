package com.reyaz.feature.result.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.reyaz.feature.result.domain.model.ResultItem
import com.reyaz.feature.result.presentation.ResultEvent

@Composable
fun ListContainerComposable(
    items: List<ResultItem>,
    onEvent: (ResultEvent) -> Unit,
    courseId: String,
    openPdf: (String) -> Unit,
) {

    Column {
        if (items.isNotEmpty())
            items.forEachIndexed { index, item ->
                DownloadableListItemComposable(
                    item = item,
                    openPdf = { openPdf(item.localPath ?: "") },
                    toggleDownload = { link, path ->
                        onEvent(
                            ResultEvent.ToggleDownload(
                                path = path,
                                url = link,
                                listId = item.listId,
                                title = item.listTitle
                            )
                        )
                    })
            }
        else
            Text("No list is yet released for this course.")
    }
}
