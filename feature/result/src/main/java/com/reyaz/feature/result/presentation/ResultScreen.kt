package com.reyaz.feature.result.presentation

import android.content.ActivityNotFoundException
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.theme.MilliaConnectTheme
import com.reyaz.feature.result.domain.model.ResultHistory
import com.reyaz.feature.result.domain.model.ResultList
import com.reyaz.feature.result.presentation.components.DropDownComposable


private const val TAG = "RESULT_SCREEN"

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    uiState: ResultUiState,
    onEvent: (ResultEvent) -> Unit = {},
    onNavigateBack: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(16.dp)/*.verticalScroll(rememberScrollState())*/,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DropDownComposable(
                    options = uiState.courseTypeList.map { it.name },
                    label = "Course Type",
                    onOptionSelected = {
                        onEvent(ResultEvent.UpdateType(it))
                        onEvent(ResultEvent.LoadCourse)
                    },
                    isLoading = uiState.typeLoading,
                    enabled = uiState.courseTypeEnabled,
                    value = uiState.selectedType
                )
                DropDownComposable(
                    options = uiState.courseNameList.map { it.name },
                    label = "Course",
                    onOptionSelected = { onEvent(ResultEvent.UpdateCourse(it)) },
                    isLoading = uiState.courseLoading,
                    enabled = uiState.courseEnabled,
                    value = uiState.selectedCourse
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onEvent(ResultEvent.LoadResult) },
                    enabled = uiState.btnEnabled
                ) {
                    Text(text = "Track Result", fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Text("Recent Searches")
        }
        items(uiState.historyList) {
            ResultCard(
                items = it.resultList,
                courseName = it.courseName
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    courseName: String = "B64 - Four Year B.Sc. (Multidisciplinary))",
    items: List<ResultList>

) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
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
                TrailingIcon(expanded)
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
                Column {
                    if (items.isNotEmpty())
                        items.forEachIndexed { index, item ->
                            Column {
//                                Text(item.listId, fontSize = 14.sp)
                                //Text(item.date, fontSize = 12.sp, fontWeight = FontWeight.Bold)
//                                if (index != items.lastIndex)
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
        }
    }
}

@Composable
fun TrailingIcon(expanded: Boolean) {
    Icon(
        Icons.Outlined.KeyboardArrowDown,
        null,
        Modifier.rotate(if (expanded) 180f else 0f)
    )
}

/*
@Preview(showSystemUi = true)
@Composable
fun ResultScreenPreview() {
    MilliaConnectTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ResultCard()
            ResultCard()
        }
    }
}
*/

