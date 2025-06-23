package com.reyaz.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun textWithIndicator(
    text: String,
    hasNewResults: Boolean
): Pair<AnnotatedString, Map<String, InlineTextContent>> {
    val indicatorId = "new_result_indicator"

    val annotatedText = buildAnnotatedString {
        append(text)
        if (hasNewResults) {
            appendInlineContent(indicatorId, "[dot]") // Placeholder text
        }
    }

    val inlineContent = mapOf(
        indicatorId to InlineTextContent(
            Placeholder(
                width = 14.sp,
                height = 8.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(8.dp)
                    .background(MaterialTheme.colorScheme.error, shape = CircleShape)
            )
        }
    )
    return Pair(annotatedText, inlineContent)
}
