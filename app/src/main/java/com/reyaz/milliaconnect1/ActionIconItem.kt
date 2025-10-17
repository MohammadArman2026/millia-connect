package com.reyaz.milliaconnect1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ActionIconItem(val route: String) {
    data class IconButtonItem(
        val actionRoute: String,
        val icon: ImageVector,
        val onClick: () -> Unit,
        val contentDescription: String = ""
    ) : ActionIconItem(actionRoute)

    data class CustomComposableItem(
        val actionRoute: String,
        val content: @Composable () -> Unit
    ) : ActionIconItem(actionRoute)
}