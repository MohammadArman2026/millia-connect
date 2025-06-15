package com.reyaz.feature.result.presentation

import com.reyaz.feature.result.domain.model.CourseType
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.ResultHistory

data class ResultUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val typeLoading: Boolean = true,
    val courseNameList: List<CourseName> = emptyList(),
    val selectedType: String = "",

    val courseLoading: Boolean = true,
    val courseTypeList: List<CourseType> = emptyList(),
    val selectedCourse: String = "",

    val historyList: List<ResultHistory> = emptyList(),
) {
    val degreeEnabled: Boolean = courseNameList.isNotEmpty()
    val courseEnabled: Boolean = selectedType.isNotEmpty() && courseTypeList.isNotEmpty()
    val btnEnabled: Boolean = selectedType.isNotEmpty() && selectedCourse.isNotEmpty()
}

