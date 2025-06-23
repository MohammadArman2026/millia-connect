package com.reyaz.feature.notice.data.model

sealed class NoticeType(
    val url: String,
    val typeId: String,
) {
    data object AcademicCalendar : NoticeType(
        url = "https://jmi.ac.in/ACADEMICS/Academic-Calendar/Academic-Calendar",
        typeId = "CAL"
    )

    data object Holiday : NoticeType(
        url = "https://jmi.ac.in/ACADEMICS/Academic-Calendar/List-Of-Holidays",
        typeId = "HOL"
    )

    data object Admission : NoticeType(
        url = "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest",
        typeId = "ADMISSION"
    )

    data object Examination : NoticeType(
        url = "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/6",
        typeId = "EXAMINATION"
    )

    data object General : NoticeType(
        url = "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/9",
        typeId = "GENERAL"
    )
    data object Academics : NoticeType(
        url = "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/10",
        typeId = "ACADEMICS"
    )
    data object Urgent : NoticeType(
        url = "https://jmicoe.in/",
        typeId = "URGENT"
    )
}