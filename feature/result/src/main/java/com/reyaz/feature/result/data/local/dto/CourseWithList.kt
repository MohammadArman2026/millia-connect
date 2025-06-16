package com.reyaz.feature.result.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.reyaz.feature.result.data.local.entities.CourseEntity
import com.reyaz.feature.result.data.local.entities.ResultListEntity

data class CourseWithList(
    @Embedded val course: CourseEntity, //@Embedded tells Room to treat all the columns of CourseEntity as part of this data class.
    @Relation(
        parentColumn = "courseId",
        entityColumn = "listOwnerId"
    )
    val lists: List<ResultListEntity>

    /**
     * @Relation(...) val resultList: List<ResultListEntity>
     * This is the key part that sets up the relationship.
     *
     *
     * You're telling Room:
     * For the given Course.courseId (parent),
     * Fetch all rows in CourseEntity (child table) where CourseEntity.courseOwnerId == Course.courseId
     * And return them as a list of ResultListEntity.
     *
     * parentColumn:	The column in CourseEntity (the parent) that Room will match against
     * entityColumn:	The column in ResultListEntity (the child) that points back to the parent
     * parentColumn     is set to the name of the primary key column of the parent entity and entityColumn set to the name of the column of the child entity that references the parent entity's primary key.
     * */
)