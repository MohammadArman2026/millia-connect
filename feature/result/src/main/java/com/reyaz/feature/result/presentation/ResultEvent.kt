package com.reyaz.feature.result.presentation

sealed class ResultEvent {
    data object LoadDegree : ResultEvent()
    data class UpdateType(val typeIndex: Int) : ResultEvent()
    data object LoadCourse : ResultEvent()
    data class UpdateCourse(val selectedIndex: Int) : ResultEvent()
    data object LoadResult : ResultEvent()
    data object LoadSavedResults: ResultEvent()
}