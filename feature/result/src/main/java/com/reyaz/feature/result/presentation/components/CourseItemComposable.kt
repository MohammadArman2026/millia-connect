package com.reyaz.feature.result.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.SpatialTracking
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.result.domain.model.ResultList
import com.reyaz.feature.result.presentation.ResultEvent
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseItemComposable(
    modifier: Modifier = Modifier,
    courseName: String = "",
    resultList: List<ResultList>,
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

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            expanded = !expanded
            if (!expanded)
                isExpandedFirstTime = true
            if (isExpandedFirstTime && hasNewResults)
                onResultEvent(ResultEvent.MarkAsRead(courseId))
        }

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
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                            .clickable {
                                expanded = false
                                onResultEvent(ResultEvent.DeleteCourse(courseId)) }
                            .padding(horizontal = 16.dp)
                            .align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Outlined.SpatialTracking,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.error,
                        )
                        Text(
                            modifier = Modifier.padding(top = 2.dp),
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