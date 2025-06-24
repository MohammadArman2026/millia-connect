package com.reyaz.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListItemWithTrailingIcon(
    listTitle: String,
    date: String?,
    trailingIcon: @Composable() (() -> Unit)?,
    onClick: () -> Unit,
    isNewItem: Boolean,
) {
    val textWithIndicator = textWithIndicator(listTitle, isNewItem)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    onClick()
                }
                .padding( 12.dp, 12.dp, 0.dp, 12.dp),
        ) {
            // result list title
            Text(
                text = textWithIndicator.first,
                inlineContent = textWithIndicator.second,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )

            // result list item date
            date?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        // trailing icon
        trailingIcon?.let { it() }
    }
    CustomListDivider(modifier= Modifier)
}
