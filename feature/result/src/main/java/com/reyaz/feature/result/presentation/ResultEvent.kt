package com.reyaz.feature.result.presentation

sealed class ResultEvent {

    data object LoadDegree : ResultEvent()
    data class UpdateType(val typeIndex: Int) : ResultEvent()
    data object LoadCourse : ResultEvent()
    data class UpdateCourse(val selectedIndex: Int) : ResultEvent()
    data object LoadResult : ResultEvent()
    data object LoadSavedResults : ResultEvent()
    data class DeleteCourse(val courseId: String) : ResultEvent()
    data class ToggleDownload(
        val path: String? = null,
        val url: String? = null,
        val listId: String = "",
        val title: String = ""
    ) : ResultEvent()

    data class DownloadPdf(
        val pdfUrl: String, val listId: String,
        val title: String
    ) : ResultEvent()

    data object RefreshResults : ResultEvent()
    data object Initialize : ResultEvent()

    data class MarkAsRead(val courseId : String) : ResultEvent()

}