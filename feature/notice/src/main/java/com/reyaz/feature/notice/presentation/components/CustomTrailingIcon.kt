package com.reyaz.feature.notice.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTrailingIcon(
    downloadProgress: Int?,
    onIconClick: () -> Unit?,
    icon: ImageVector?,
    isDownloading: Boolean
) {
    Box(
        modifier = Modifier
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isDownloading && downloadProgress == null) {
            CircularProgressIndicator(
                modifier = Modifier.padding(4.dp),
                trackColor = MaterialTheme.colorScheme.onSecondary
            )
            Icon(Icons.Default.Downloading, "Downloading")
        } else if (downloadProgress in 1..99) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(4.dp),
                    progress = { downloadProgress!!.div(100f) },
                    trackColor = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    "${downloadProgress}%",
                    fontSize = 8.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else
                icon?.let {
                    Icon(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape)
//                        .background(Color.Cyan)
                            .clickable(onClick = { onIconClick() })
                            .padding(8.dp),
                        contentDescription = null,
                        imageVector = it
                    )
                }
    }
}
