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
    GENERAL(title = "General", type = NoticeType.General),
    CALENDAR(title = "Calendar", type = NoticeType.AcademicCalendar),
    URGENT(title = "Urgent", type = NoticeType.Urgent),
    ACADEMICS(title = "Academics", type = NoticeType.Academics),
    EXAMINATION(title = "Examination", type = NoticeType.Examination),
    ADMISSION(title = "Admission", type = NoticeType.Admission),
    HOSTEL(title = "Holiday", type = NoticeType.Holiday),
}

