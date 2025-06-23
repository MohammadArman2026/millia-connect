package com.reyaz.feature.result.presentation.components

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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.result.domain.model.ResultItem

@Composable
fun DownloadableListItemComposable(
    item: ResultItem,
    toggleDownload: (String?, String?) -> Unit, // link, path
    openPdf: () -> Unit,
) {
    val textWithIndicator = textWithIndicator(item.listTitle, !item.viewed)

    ResultListDivider()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
//                    .padding(end = 8.dp)
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    if(item.isDownloadable){
                        item.localPath?.let {
                            openPdf()
                        } ?: run {
                            // todo: check if the given link is ending at .pdf else open the link
                            toggleDownload(item.link, null)
                        }
                    } else {
                        // todo: open the link in browser
                    }
                }
                .padding(start = 8.dp),
        ) {
            // result list title
            Text(
                text = textWithIndicator.first,
                inlineContent = textWithIndicator.second,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                )

            // result list item date
            item.date?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        // trailing icon
        if (!item.link.isNullOrEmpty()) {
            if (item.isDownloadable)
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
                        Text(
                            "${item.downloadProgress}%",
                            fontSize = 8.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
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
            else {
                // open link
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "",
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
