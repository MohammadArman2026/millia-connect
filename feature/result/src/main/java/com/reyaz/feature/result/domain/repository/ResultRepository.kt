package com.reyaz.feature.result.domain.repository

import com.reyaz.feature.result.domain.model.CourseType

interface ResultRepository {
    suspend fun getDegree(): Result<List<CourseType>>
//    suspend fun getCourse(degree: String): List<String>
//    suspend fun getResult(degree: String, course: String): List<String>
}