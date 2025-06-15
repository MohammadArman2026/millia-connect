package com.reyaz.feature.result.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownWithLoader(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    selectedText: String = "",
    items: List<String> = emptyList(),
    onItemSelected: (String) -> Unit,
    label: String = "label"
) {
    var expanded by remember { mutableStateOf(false) }
    //var selectedText by remember { mutableStateOf("") }

    // Filtered course list based on input
    val filteredCourses = remember(selectedText) {
        if (selectedText.isEmpty()) items
        else items.filter { it.contains(selectedText, ignoreCase = true) }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
//            expanded = true // Always open on click
            expanded = !expanded
        }
    ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {
                    onItemSelected(it)
                    expanded = true // Keep dropdown open as user types
                },
                label = { Text(label) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded && filteredCourses.isNotEmpty(),
                onDismissRequest = { expanded = false }
            ) {
                Box(
                    modifier = Modifier
                        .heightIn(max = 200.dp)
                        .fillMaxWidth()
                ) {
                    LazyColumn {
                        items(filteredCourses) { course ->
                            DropdownMenuItem(
                                text = { Text(course) },
                                onClick = {
                                    onItemSelected(course)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

    }
}