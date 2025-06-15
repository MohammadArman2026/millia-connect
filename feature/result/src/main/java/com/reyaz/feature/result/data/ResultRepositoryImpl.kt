package com.reyaz.feature.result.data

import android.util.Log
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import com.reyaz.feature.result.domain.model.ResultHistory
import com.reyaz.feature.result.domain.repository.ResultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "RESULT_REPO_IMPL"

class ResultRepositoryImpl(
    private val resultScraper: ResultScraper
//    private val resultScraper: DropdownSelector
) : ResultRepository {

    override suspend fun getCourseTypes(): Result<List<CourseType>> =
        withContext(Dispatchers.IO) {
            try {
//                resultScraper.getProgramsForCourseType() // Java method
                val response: Result<List<CourseType>> =
                    resultScraper.fetchPrograms() // Java method
                Result.success(response.getOrDefault(emptyList()))
            } catch (e: Exception) {
                Log.e(TAG, "Exception while scraping: ${e.message}")
                Result.failure(e)
            }
        }

    override suspend fun getCourses(type: String): Result<List<CourseName>> = withContext(Dispatchers.IO) {
         try {
            val request: Result<List<CourseName>> = resultScraper.fetchPrograms(type)
            if (request.isSuccess) {
                Result.success(request.getOrDefault(emptyList()))
            } else {
                throw request.exceptionOrNull()!!
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getResult(type: String, course: String, phdDepartment: String): Result<List<ResultHistory>> = withContext(Dispatchers.IO) {
         try {
            val result: Result<List<ResultHistory>> = resultScraper.fetchResult(courseType = type, courseName = course, phdDiscipline = phdDepartment)
            Result.success(result.getOrDefault(emptyList()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}