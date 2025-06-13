package com.reyaz.feature.portal.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    imeAction: ImeAction = ImeAction.Done,
    onDone: () -> Unit = {},
    leadingIcon: @Composable() (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var isFocused by remember { mutableStateOf(false) }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary), // Add cursor brush here
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused }
            .fillMaxWidth()
            .clip(shape = CircleShape)
            .background(TextFieldDefaults.colors().unfocusedContainerColor.copy(alpha = 0.1f))
            .border(
                width = 1.5.dp,
                brush = if (isFocused || value.isNotEmpty()) SolidColor(MaterialTheme.colorScheme.primary) else SolidColor(
                    TextFieldDefaults.colors().unfocusedIndicatorColor.copy(alpha = 0.2f)
                ),
                shape = CircleShape
            )
            .padding(vertical = 8.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp) // Add horizontal padding to the row
                .fillMaxWidth(),
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) leadingIcon()

            if (value.isEmpty() && !isFocused)
                Text(
                    text = label, color = TextFieldDefaults.colors().unfocusedPlaceholderColor,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            else
                it() // This displays the text with cursor

            Spacer(Modifier.weight(1f))
            if (value.isNotEmpty())
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(16.dp)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.error, CircleShape)
                        .clip(CircleShape)
                        .size(24.dp)
//                        .fillMaxWidth()
                        .clickable { /*onValueChange("")*/ }
                )
        }
    }
}