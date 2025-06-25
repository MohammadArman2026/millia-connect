package com.reyaz.feature.notice.domain.model

import com.reyaz.feature.notice.data.model.NoticeType

enum class TabConfig(
    val title: String,
    val type: NoticeType
){
    CALENDAR(title = "Calendar", type = NoticeType.AcademicCalendar),
    GENERAL(title = "General", type = NoticeType.General),
    HOLIDAY(title = "Holiday", type = NoticeType.Holiday),
    URGENT(title = "Urgent", type = NoticeType.Urgent),
    EXAMINATION(title = "Examination", type = NoticeType.Examination),
    ACADEMICS(title = "Academics", type = NoticeType.Academics),
    ADMISSION(title = "Admission", type = NoticeType.Admission),
    HOSTEL(title = "Hostel", type = NoticeType.Hostel),
    NRI(title = "NRI", type = NoticeType.NRI),
}

