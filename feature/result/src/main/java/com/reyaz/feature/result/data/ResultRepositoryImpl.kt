package com.reyaz.feature.result.data

import android.util.Log
import com.reyaz.feature.result.domain.repository.ResultRepository

private const val TAG = "RESULT_REPO_IMPL"
class ResultRepositoryImpl(
    private val resultScraper: ResultScraper
) : ResultRepository {
    override suspend fun getDegree(): Result<List<String>>{
        return try {
//            val request =
                resultScraper.fetchDegree()
//            Log.d("ResultRepositoryImpl", "getDegree: $request")
            Result.success(emptyList())
        } catch (e: Exception) {
            TODO("Not yet implemented")
        }
    }
  /*  override suspend fun getCourse(degree: String): List<String>{

    }
    override suspend fun getResult(degree: String, course: String): List<String>{

    }*/
}