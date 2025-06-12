package com.reyaz.feature.portal.presentation.components

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun gradientBrush(alpha: Float = 1f): Brush {
    return Brush.linearGradient(
        colors = listOf(Color(0xFF223876).copy(1f), Color(0xFF1e429f).copy(alpha), Color(0xFF5C1ED9).copy(alpha))
    )
}