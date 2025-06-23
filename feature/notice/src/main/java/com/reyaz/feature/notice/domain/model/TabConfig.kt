package com.reyaz.feature.notice.domain.model

import com.reyaz.feature.notice.data.model.NoticeType

//
//data class TabConfig(
//    val noticeType : String,
//    val tabName : String,
//    val unReadCount : Int = 0,
//    val isSelected : Boolean,
//    val notices : List<Notice>
//)

enum class Tabs(
    val title: String,
    val type: NoticeType
){
    HOSTEL(title = "Holiday", type = NoticeType.Holiday),
    CALENDAR(title = "Calendar", type = NoticeType.AcademicCalendar),
}

