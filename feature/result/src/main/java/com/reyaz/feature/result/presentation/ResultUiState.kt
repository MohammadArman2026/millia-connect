package com.reyaz.feature.result.presentation

import com.reyaz.feature.result.domain.model.CourseType
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.ResultHistory

data class ResultUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val typeLoading: Boolean = true,
    val courseTypeList: List<CourseType> = emptyList(),
    val selectedTypeIndex: Int? = null,

    val courseLoading: Boolean = true,
    val courseNameList: List<CourseName> = emptyList(),
    val selectedCourseIndex: Int? = null,

    val historyList: List<ResultHistory> = emptyList(),
) {
    val selectedType: String = selectedTypeIndex?.let { courseTypeList[it].name } ?: ""
    val selectedTypeId: String = selectedTypeIndex?.let { courseTypeList[it].id } ?: ""

    val selectedCourse: String = selectedCourseIndex?.let { courseNameList[it].name } ?: ""
    val selectedCourseId: String = selectedCourseIndex?.let { courseNameList[it].id } ?: ""

    val courseTypeEnabled: Boolean = courseTypeList.isNotEmpty() && !typeLoading
    val courseEnabled: Boolean =
        selectedTypeIndex != null && courseNameList.isNotEmpty() && !courseLoading
    val btnEnabled: Boolean = selectedTypeIndex != null && selectedCourseIndex != null
}

