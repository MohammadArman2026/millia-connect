package com.reyaz.feature.result.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CourseEntity(
    @PrimaryKey
    val courseId: String,
    val courseName: String,
    val courseTypeId: String,
    val courseType: String,
    val phdDepartmentId: String? = null,
    val phdDepartment: String? = null,
    val trackEnabled: Boolean = true
)
