package com.reyaz.feature.result.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.reyaz.feature.result.data.local.dto.CourseWithList
import com.reyaz.feature.result.data.local.entities.CourseEntity
import com.reyaz.feature.result.data.local.entities.ResultListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {
    @Transaction
    @Query("SELECT * FROM CourseEntity")
    fun observeResults(): Flow<List<CourseWithList>>

    @Query("SELECT * FROM CourseEntity")
    suspend fun getCourses(): List<CourseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResultList(detail: ResultListEntity)

    @Query("SELECT * FROM ResultListEntity WHERE listOwnerId = :courseId")
    suspend fun getResults(courseId: String): List<ResultListEntity>

    // update course tracking
    @Query("UPDATE CourseEntity SET trackEnabled = :status WHERE courseId = :courseId")
    suspend fun updateTrackingStatus(status: Boolean, courseId: String)

    // delete course
    @Query("DELETE FROM CourseEntity WHERE courseId = :courseId")
    suspend fun deleteCourse(courseId: String)

}