package com.reyaz.feature.result.domain.model

data class ResultHistory(
    val courseId: String = "",
    val courseName: String = "",
    val courseType: String = "",
    val resultList: List<ResultItem> = emptyList(),

    val link: String? = "",
    val syncDate: String?,
    val remarks: String = "",
    val latestListDate: String?
) {
    val hasNewResult: Boolean = resultList.any { list -> !list.viewed }
}

