package com.reyaz.feature.result.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    primaryKeys = ["listId", "listOwnerId"], // Composite primary key
    foreignKeys = [ForeignKey(
        entity = CourseEntity::class,
        parentColumns = ["courseId"],
        childColumns = ["listOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("listOwnerId")]
)
data class ResultListEntity(
//    @PrimaryKey
    val listId: String,
    val remark: String,
    val releaseDate: Long?,
    val viewed: Boolean = false,
    val link: String?,
    val pdfPath: String?,
    val downloadProgress: Int? = null,

    val listOwnerId: String     // FK to CourseEntity.courseId
)
