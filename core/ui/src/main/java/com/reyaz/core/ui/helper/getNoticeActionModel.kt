package com.reyaz.core.ui.helper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.OpenInNew
import com.reyaz.core.ui.model.ListItemUiModel

fun getListItemModel(
    link: String?,
    path: String?,
    downloadPdf: () -> Unit,
    deletePdf: () -> Unit,
    openLink: () -> Unit,
): ListItemUiModel {
    return when {
        link == null -> ListItemUiModel(icon = null, onClick = null)

        link.endsWith(".pdf") && path == null -> ListItemUiModel(
            icon = Icons.Outlined.Download,
            onClick = downloadPdf
        )
        link.endsWith(".pdf") && path != null -> ListItemUiModel(
            icon = Icons.Outlined.Delete,
            onClick = deletePdf // for trailing icon
        )
        else -> ListItemUiModel(
            icon = Icons.AutoMirrored.Outlined.OpenInNew,
            onClick = openLink
        )
    }
}
