package com.reyaz.feature.result.domain.model

data class ResultHistory(
    val id: String = "",
    val degreeName: String = "",
    val courseName: String = "",        // todo: remove
    val link: String? = "",
    val date: String = "",
    val remarks: String = ""
)