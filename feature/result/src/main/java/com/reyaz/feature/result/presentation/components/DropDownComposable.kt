package com.reyaz.feature.result.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DropDownComposable(
    modifier: Modifier = Modifier,
    value: String = "",
    options: List<String>,
    label: String,
    onOptionSelected: (Int) -> Unit,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
//    var selectedOptionText by rememberSaveable { mutableStateOf("") }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { if(enabled) expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = value,
            onValueChange = {},
            placeholder = { Text(label) },
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            enabled = enabled
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
//                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(text = selectionOption) },
                    onClick = {
//                        selectedOptionText = selectionOption
                        expanded = false
                        onOptionSelected(index)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
                if (index != options.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}