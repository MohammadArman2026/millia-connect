package com.reyaz.feature.rent.presentation.property_post_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DescriptionField(
    title:String,
    example:String,
    description: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier=Modifier.fillMaxWidth()){
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 12.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                onValueChange(it) },
            placeholder = {
                Text(
                    text = description.ifEmpty {
                        example
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(start = 12.dp, end = 12.dp, top = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray
            ),
            maxLines = Int.MAX_VALUE,
            singleLine = false,
        )
    }
}
