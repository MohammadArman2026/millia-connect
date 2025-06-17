package com.reyaz.feature.result.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = CourseEntity::class,
        parentColumns = ["courseId"],
        childColumns = ["listOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("listOwnerId")]
)
data class ResultListEntity(
    @PrimaryKey
    val listId: String,
    val remark: String,
    val date: Date = Date(), // current time
    val viewed: Boolean = false,
    val link: String?,
    val pdfPath: String?,

    val listOwnerId: String     // FK to CourseEntity.courseId
)
