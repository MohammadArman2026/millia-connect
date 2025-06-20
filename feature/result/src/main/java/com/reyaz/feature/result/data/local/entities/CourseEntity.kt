package com.reyaz.feature.result.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class CourseEntity(
    @PrimaryKey
    val courseId: String,
    val courseName: String,
    val courseTypeId: String,
    val courseType: String,
    val phdDepartmentId: String? = null,
    val phdDepartment: String? = null,
    val trackEnabled: Boolean = true,
    val lastSync: Long?,      //Room stores primitive types like long more efficiently because they’re directly supported by SQLite. Avoids Type Converters: You don’t need to define a custom TypeConverter because you’re working with a native SQLite type.
    val createdAt: Long = Date().time
)
