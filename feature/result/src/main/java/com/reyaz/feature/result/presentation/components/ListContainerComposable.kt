package com.reyaz.feature.result.presentation.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.SpatialTracking
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.result.domain.model.ResultList
import com.reyaz.feature.result.presentation.ResultEvent

@Composable
fun ListContainerComposable(
    items: List<ResultList>,
    onEvent: (ResultEvent) -> Unit,
    courseId: String,
    openPdf: (String) -> Unit,
) {

    Column {
        if (items.isNotEmpty())
            items.forEachIndexed { index, item ->
                ListItemComposable(
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

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onEvent(ResultEvent.DeleteCourse(courseId)) }
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Outlined.SpatialTracking,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error,
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = "Stop tracking",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }
//        }
    }
}

@Composable
private fun ListItemComposable(
    item: ResultList,
    toggleDownload: (String?, String?) -> Unit, // link, path
    openPdf: ()->Unit,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    Column {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                item.listTitle,
                fontSize = 14.sp,
                modifier = Modifier
//                    .padding(end = 8.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        item.localPath?.let {
                            // open pdf
                            openPdf()
                        } ?: run {
                            //Log.d("LIST_ITEM_COMPOSABLE", "ListItemComposable: ${item.link}")
                            // download pdf
                            // todo: check if the given link is ending at .pdf else open the link
                            toggleDownload(item.link, null)
                        }
//                        if ()
                    }
                    .padding(start = 8.dp),
            )

            if (!item.link.isNullOrEmpty()) {
                if (item.localPath.isNullOrEmpty()) {
                    if (item.downloadProgress != null && (item.downloadProgress in 0..99)) {
                        // circular progress
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(28.dp)
                        ) {
                            CircularProgressIndicator(
//                                modifier = Modifier.size(16.dp).padding(12.dp),
                                progress = { item.downloadProgress.div(100f) },
                                trackColor = MaterialTheme.colorScheme.onSecondary
                            )
                            Text("${item.downloadProgress}%", fontSize = 8.sp, modifier = Modifier.align(Alignment.Center))
                        }
                    } else {
                        // download btn
                        Icon(
                            imageVector = Icons.Default.ArrowCircleDown, contentDescription = "",
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { toggleDownload(item.link, null) }
                                .padding(12.dp),
                        )
                    }
                } else {
                    // delete btn
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = "",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { toggleDownload(null, item.localPath) }
                            .padding(12.dp),
                    )
                }
            }
        }
    }
}