package com.reyaz.feature.result.domain.repository

interface ResultRepository {
    suspend fun getDegree(): Result<List<String>>
//    suspend fun getCourse(degree: String): List<String>
//    suspend fun getResult(degree: String, course: String): List<String>
}