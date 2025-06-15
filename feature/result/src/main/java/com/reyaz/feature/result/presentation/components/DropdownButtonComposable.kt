package com.reyaz.feature.result.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropdownButtonComposable(
    modifier: Modifier = Modifier,
    text: String = "label",
    options: List<String>,
    isLoadingOption: Boolean = false,
    onOptionSelected: (String) -> Unit = {},
    onAddItemClicked: () -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column {
        OutlinedButton(
            modifier = modifier
                .fillMaxWidth()
                .height(OutlinedTextFieldDefaults.MinHeight),
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isLoadingOption) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = if (!expanded) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = null
                    )
                }
            }
        }
        Box(
            modifier = Modifier.align(Alignment.End),
            contentAlignment = Alignment.TopEnd
        ) {
            DropdownMenu(
                modifier = Modifier.padding(8.dp),
                expanded = expanded,
                onDismissRequest = { expanded = !expanded }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = option, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add button"
                        )
                    },
                    text = { Text("Add New") },
                    onClick = { onAddItemClicked() }
                )
            }
        }
    }
}