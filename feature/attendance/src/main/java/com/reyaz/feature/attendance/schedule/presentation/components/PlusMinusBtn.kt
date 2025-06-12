package com.reyaz.feature.attendance.schedule.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun PlusMinusBtn(
    displayText: String,
    increase: () -> Unit,
    decrease: () -> Unit,
    isMinusEnabled: Boolean = true,
    isPlusEnabled: Boolean = true
) {
    val iconBtnColor = IconButtonColors(
        contentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
        disabledContentColor = MaterialTheme.colorScheme.onPrimary
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            colors = iconBtnColor,
            enabled = isMinusEnabled,
            onClick = decrease,
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Remove, contentDescription = "minus",
//                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(displayText)
        IconButton(
            colors = iconBtnColor,
            enabled = isPlusEnabled,
            onClick = increase,
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "add",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}