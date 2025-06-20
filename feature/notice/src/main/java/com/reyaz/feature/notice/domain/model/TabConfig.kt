package com.reyaz.feature.notice.domain.model

data class TabConfig(
    val noticeType : String,
    val tabName : String,
    val unReadCount : Int,
    val isSelected : Boolean,
    val notices : List<Notice>
)