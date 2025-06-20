package com.reyaz.feature.result.domain.model

data class ResultHistory(
    val courseId: String = "",
    val courseName: String = "",
    val courseType: String = "",
    val resultList: List<ResultList> = emptyList(),

    val link: String? = "",
    val syncDate: String?,
    val remarks: String = "",
    val latestListDate: String?
) {
    val hasNewResult: Boolean = resultList.any { list -> !list.viewed }
}

data class ResultList(
    val listId: String = "",
    val listTitle: String = "",
    val link: String? = null,
    val date: String?,
    val localPath: String? = null,
    val downloadProgress: Int? = null,
    val viewed: Boolean = true
)