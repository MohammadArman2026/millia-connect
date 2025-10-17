package com.reyaz.feature.rent.presentation.property_post_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextField(
    title: String,
    example: String,
    propertyValue: String,
    onValueChange:(String)->Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 12.dp)
        )
        OutlinedTextField(
            value =propertyValue,
            onValueChange = {
                onValueChange(it)
            },
            placeholder = { Text(
                text = propertyValue.ifEmpty {
                    example
                }
            ) },
            modifier=Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(start=12.dp,end=12.dp,top=4.dp),
            shape= RoundedCornerShape(8.dp),
            singleLine = true,
            maxLines = 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
                unfocusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = if(title.contains("Floor") ||
                    title.contains("Total") ||
                    title.contains("Rent") ||
                    title.contains("Security")){
                    KeyboardType.Number
                }else{
                    KeyboardType.Text
                },
                imeAction = ImeAction.Next         // keyboard action button
            )
        )
    }
}