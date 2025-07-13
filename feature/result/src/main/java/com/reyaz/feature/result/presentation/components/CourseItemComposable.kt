package com.reyaz.feature.result.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.SpatialTracking
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.common.utils.shareTextList
import com.reyaz.feature.result.domain.model.ResultItem
import com.reyaz.feature.result.presentation.ResultEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseItemComposable(
    modifier: Modifier = Modifier,
    courseName: String = "",
    resultList: List<ResultItem>,
    onResultEvent: (ResultEvent) -> Unit,
    courseId: String,
    openPdf: (String) -> Unit,
    lastSync: String?,
    mostRecent: String?,
    hasNewResults: Boolean

) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isExpandedFirstTime by rememberSaveable { mutableStateOf(false) }
    val textWithIndicator = textWithIndicator(courseName, hasNewResults)
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            expanded = !expanded
            if (!expanded)
                isExpandedFirstTime = true
            if (isExpandedFirstTime && hasNewResults)
                onResultEvent(ResultEvent.MarkAsRead(courseId))
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            //verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // course name + red dot
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = textWithIndicator.first,
                        inlineContent = textWithIndicator.second,
                        fontWeight = FontWeight.Bold,
                    )
                }

                // trailing dropdown icon
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }

            // timeline
            AnimatedVisibility(!expanded) {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // fetched time duration
                    lastSync?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Sync,
                                "last fetch",
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 4.dp)
                            )
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    mostRecent?.let {
                        Text(
                            text = "Latest: $it",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // results list
            AnimatedVisibility(expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    ListContainerComposable(
                        items = resultList,
                        onEvent = onResultEvent,
                        courseId = courseId,
                        openPdf = openPdf
                    )

                    // divider below result list
                    ResultListDivider()

                    // stop tracking btn
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(horizontal = 16.dp)
                            .align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // share result button
                        if (resultList.isNotEmpty()) {
                            TextButton(
//                                modifier = Modifier.weight(1f),
                                onClick = {
                                    val sharableList = listOf(courseName) +
                                            resultList.map { "◉ ${it.listTitle}: ${it.link}" }
                                    context.shareTextList(sharableList)
                                },
                                contentPadding = PaddingValues(8.dp, 0.dp, 32.dp, 0.dp)
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(end = 4.dp),
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                                Text(
                                    modifier = Modifier.padding(top = 2.dp, start = 4.dp),
                                    text = "Share",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))

                        // stop tracking btn
                        TextButton(
//                            modifier = Modifier.weight(1f),
                            onClick = {
                                expanded = false
                                onResultEvent(ResultEvent.DeleteCourse(courseId))
                            },
                            contentPadding = PaddingValues(32.dp, 0.dp, 8.dp, 0.dp)
                        ){
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Outlined.SpatialTracking,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.error,
                            )
                            Text(
                                modifier = Modifier.padding(top = 2.dp, start = 4.dp),
                                text = "Stop tracking",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}