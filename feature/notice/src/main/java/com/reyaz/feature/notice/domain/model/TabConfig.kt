package com.reyaz.feature.notice.domain.model

import com.reyaz.feature.notice.data.model.NoticeType

enum class TabConfig(
    val title: String,
    val type: NoticeType
){
    CALENDAR(title = "Calendar", type = NoticeType.AcademicCalendar),
    HOSTEL(title = "Hostel", type = NoticeType.Hostel),
    GENERAL(title = "General", type = NoticeType.General),
    URGENT(title = "Urgent", type = NoticeType.Urgent),
    ACADEMICS(title = "Academics", type = NoticeType.Academics),
    EXAMINATION(title = "Examination", type = NoticeType.Examination),
    ADMISSION(title = "Admission", type = NoticeType.Admission),
    HOLIDAY(title = "Holiday", type = NoticeType.Holiday),
    NRI(title = "NRI", type = NoticeType.NRI),
}

