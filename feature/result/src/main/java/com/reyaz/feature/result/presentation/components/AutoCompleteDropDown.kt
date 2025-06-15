package com.reyaz.feature.result.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.reyaz.core.ui.theme.MilliaConnectTheme
import com.reyaz.feature.result.presentation.ResultScreen
import com.reyaz.feature.result.presentation.ResultUiState

@Composable
fun AutoCompleteDropDown(
    modifier: Modifier = Modifier,
    list: List<String>,
    selectedText: String,
    onItemSelected: (String) -> Unit,
    isLoading: Boolean = false,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardOptions = KeyboardOptions.Default
) {
    var selectionDone by remember { mutableStateOf(false) }

    val heightTextFields by remember {
        mutableStateOf(64.dp)
    }
    var textValue by remember {mutableStateOf("")}
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    // Category Field
    Column(
        modifier = Modifier
            //.padding(30.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTextFields)
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    value = selectedText,
                    onValueChange = {
                        textValue = it
                        expanded = true
                    },
                    textStyle = TextStyle(
//                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    label = { Text(label) },
                    keyboardOptions = keyboardOptions,
//                    keyboardActions = keyboardActions,
                    singleLine = true,
                    trailingIcon = {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        } else {
                            IconButton(onClick = { if (selectionDone){ textValue = "" } else {
                                expanded = !expanded
                            }
                            }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else if(selectionDone)Icons.Rounded.Clear else Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = "arrow",
                                )
                            }
                        }
                    }
                )
            }

            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize.width.dp),
                    elevation = CardDefaults.cardElevation(15.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {

                    LazyColumn(
//                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {

                        if (textValue.isNotEmpty()) {
                            items(
                                list.filter { listItem ->
                                    listItem.lowercase().contains(selectedText.lowercase())
                                }.sorted()
                            ) {
                                CategoryItems(title = it) { title ->
                                    textValue = title
                                    onItemSelected(title)
                                    expanded = false
                                    selectionDone = true
                                }
                            }
                        } else {
                            items(
                                list.sorted()
                            ) {
                                CategoryItems(title = it) { title ->
                                    textValue = title
                                    onItemSelected( "UG1")
                                    expanded = false
                                    selectionDone = true
                                }
                            }
                        }

                    }

                }
            }

        }

    }


}

@Composable
fun CategoryItems(
    title: String,
    onSelect: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }

}



@Preview()
@Composable
fun AutoCompletePreview() {
    MilliaConnectTheme(darkTheme = false) {
        AutoCompleteDropDown(
            list = listOf("hello", "baby"),
            isLoading = false,
            selectedText = "hello",
            onItemSelected = {},
            label = "label"
        )
    }
}
/*@Preview(showSystemUi = true)
@Composable
fun ResultScreenPreview() {
    MilliaConnectTheme(darkTheme = false) {
        ResultScreen(
            uiState = ResultUiState(),
            onNavigateBack = {}
        )
    }
}*/







