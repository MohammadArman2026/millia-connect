package com.reyaz.feature.result.data.local.dto

data class RemoteCourseResultDto(
    val courseNameId: String = "",
    val courseName: String = "",
    val courseTypeId: String = "",
    val courseType: String = "",
    val phdDepartmentId: String = "",
    val phdDepartment: String = "",
    val resultLists: List<RemoteResultListDto> = emptyList()
)