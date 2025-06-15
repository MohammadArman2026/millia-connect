package com.reyaz.feature.result.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownInsteaComposable(
    modifier: Modifier = Modifier,
    label: String = "",
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    leadingIcon: ImageVector = Icons.Default.Call,
    isError: Boolean = false,
    errorMessage: String? = null,
    selectedOption: String? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onAddItemClicked: () -> Unit = {},
    isEnabled: Boolean = true,
    isLoadingOption: Boolean,
    isExpandable: Boolean = true,
    showAddBtn:Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var expandable by remember { mutableStateOf(isExpandable) }
    var count by remember{ mutableIntStateOf(0) }

//    Log.d("IN LOADING", "$label loading: $isLoadingOption")
//    Log.d("IN EXPANDABLE", "$label loading: $expandable")

    LaunchedEffect(options) {
        if (options.isNotEmpty())
        expandable = true
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = {
                if (expandable) {
                    expanded = !expanded
                }
            }) {
            OutlinedTextField(
                modifier = modifier.menuAnchor(),
                value = selectedOption?:"",
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                enabled = !isLoadingOption && isEnabled,
                isError = isError,
                label = {
                    Text(
                        text = label,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
                trailingIcon = {
                    if (isLoadingOption) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                supportingText = {
                    // Display error text if the input is not valid
                    if (isError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = errorMessage?:"",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardActions = keyboardActions,
            )
            DropdownMenu(
                modifier = Modifier,
                expanded = expanded,
                onDismissRequest = { expanded = false }
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
                if(showAddBtn)
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add button"
                        )
                    },
                    text = { Text("Add New") },
                    onClick = { onAddItemClicked()
                    expanded = false}
                )
            }
        }
    }
}