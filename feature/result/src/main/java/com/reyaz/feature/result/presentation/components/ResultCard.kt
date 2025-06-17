package com.reyaz.feature.result.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.result.domain.model.ResultList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    courseName: String = "",
    items: List<ResultList>

) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { expanded = !expanded }

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                // course name
                Text(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    text = courseName,
                    fontWeight = FontWeight.Bold,
                )
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }

            // date
            if (items.isNotEmpty())
                AnimatedVisibility(!expanded) {
                    Text(
                        text = items.first().date,
                        modifier = Modifier.align(Alignment.End),
                        fontSize = 10.sp
                    )
                }
            AnimatedVisibility(expanded) {
                ResultListComposable(items)
            }
        }
    }
}

@Preview(showBackground = true, name = "Result Card - Expanded")
@Composable
fun ResultCardExpandedPreview() {
    Surface {
        ResultCard(
            courseName = "B.Tech. Computer Engineering (2022-2026)",
            items = listOf(
                ResultList(listTitle = "Applied Mathematics-I", link = "Grade A", date = "2023-01-15"),
                ResultList(listTitle = "Programming in C", link = "Grade B+", date = "2023-01-15"),
                ResultList(listTitle = "Digital Logic Design", link = null, date = "2023-01-15")

            )
        )
    }
}

@Preview(showBackground = true, name = "Result Card - Empty Items")
@Composable
fun ResultCardEmptyPreview() {
    Surface {
        ResultCard(
            courseName = "B.Tech. Computer Engineering (2022-2026)",
            items = emptyList() // Test with empty list
        )
    }
}