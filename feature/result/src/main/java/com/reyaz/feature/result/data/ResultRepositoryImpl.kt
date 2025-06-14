package com.reyaz.feature.result.data

import android.util.Log
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import com.reyaz.feature.result.domain.repository.ResultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private const val TAG = "RESULT_REPO_IMPL"
class ResultRepositoryImpl(
//    private val resultScraper: ResultScraper
    private val resultScraper: DropdownSelector
) : ResultRepository {
    /*override suspend fun getDegree(): Result<List<CourseType>> {
        return try {
//            val request =
//                resultScraper.fetchProgram()
                runBlocking {  resultScraper.getProgramsForCourseType("UG1")}
//            Log.d("ResultRepositoryImpl", "getDegree: $request")
            Result.success(emptyList())
        } catch (e: Exception) {
            TODO("Not yet implemented")
        }
    }*/
  /*  override suspend fun getCourse(degree: String): List<String>{

    }
    override suspend fun getResult(degree: String, course: String): List<String>{

    }*/

    override suspend fun getDegree(): Result<List<CourseType>> =
        withContext(Dispatchers.IO) {
            try {
//                resultScraper.getProgramsForCourseType() // Java method
                resultScraper.fetchProgByHardCode() // Java method
                Result.success(emptyList())
            } catch (e: Exception) {
                Log.e(TAG, "Exception while scraping: ${e.message}")
                Result.failure(e)
            }
        }

}