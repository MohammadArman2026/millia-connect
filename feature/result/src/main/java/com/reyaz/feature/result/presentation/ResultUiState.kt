package com.reyaz.feature.result.presentation

import com.reyaz.feature.result.domain.model.Course
import com.reyaz.feature.result.domain.model.Degree
import com.reyaz.feature.result.domain.model.ResultHistory

data class ResultUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val degreeLoading: Boolean = true,
    val degreeList: List<Degree> = emptyList(),
    val selectedDegree: String = "",

    val courseLoading: Boolean = true,
    val courseList: List<Course> = emptyList(),
    val selectedCourse: String = "",

    val historyList: List<ResultHistory> = emptyList(),
) {
    val degreeEnabled: Boolean = degreeList.isNotEmpty()
    val courseEnabled: Boolean = selectedDegree.isNotEmpty() && courseList.isNotEmpty()
    val btnEnabled: Boolean = selectedDegree.isNotEmpty() && selectedCourse.isNotEmpty()
}

