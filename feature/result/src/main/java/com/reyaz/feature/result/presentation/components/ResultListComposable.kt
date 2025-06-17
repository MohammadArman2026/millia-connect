package com.reyaz.feature.result.presentation.components

import android.content.ActivityNotFoundException
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.result.domain.model.ResultList

@Composable
fun ResultListComposable(
    items: List<ResultList>
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    Column {
        if (items.isNotEmpty())
            items.forEachIndexed { index, item ->
                Column {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        item.listTitle,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            val url = item.link
                            try {
                                if (url != null) {
                                    uriHandler.openUri(url)
                                } else {
                                    throw ActivityNotFoundException("No URL found")
                                }
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(
                                    context,
                                    "No app found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
//                                    color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        else
            Text("No list is yet released for this course.")

    }
}




@Preview(showBackground = true, showSystemUi = true, name = "ResultList - With Items")
@Composable
fun ResultListComposableWithItemsPreview() {
    MaterialTheme { // Apply MaterialTheme for proper colors and typography
        Surface { // Provides a surface background
            ResultListComposable(
                items = listOf(
                    ResultList(
                        listId = "101",
                        listTitle = "Mid Term Exam - Semester 3 (2024)",
                        link = "https://example.com/midterm_result_101",
                        date = "2024-10-25"
                    ),
                    ResultList(
                        listId = "102",
                        listTitle = "End Term Exam - Semester 3 (2024)",
                        link = "https://example.com/endterm_result_102",
                        date = "2025-01-10"
                    ),
                    ResultList(
                        listId = "103",
                        listTitle = "Practical Exam - Lab 5 (2024)",
                        link = null, // Test with null link
                        date = "2024-12-01"
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "ResultList - Empty")
@Composable
fun ResultListComposableEmptyPreview() {
    MaterialTheme {
        Surface {
            ResultListComposable(
                items = emptyList()
            )
        }
    }
}