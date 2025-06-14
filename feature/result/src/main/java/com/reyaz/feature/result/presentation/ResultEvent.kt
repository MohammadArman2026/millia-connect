package com.reyaz.feature.result.presentation

sealed class ResultEvent {
    data object LoadDegree : ResultEvent()
    data class UpdateDegree(val degree: String) : ResultEvent()
    data class LoadCourse(val degree: String) : ResultEvent()
    data class UpdateCourse(val course: String) : ResultEvent()
    data class LoadResult(val degree: String, val course: String) : ResultEvent()
}