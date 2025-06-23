package com.reyaz.core.ui.model

import androidx.compose.ui.graphics.vector.ImageVector


data class ListItemUiModel(
    val icon: ImageVector?,
    val onClick: (() -> Unit)?
)