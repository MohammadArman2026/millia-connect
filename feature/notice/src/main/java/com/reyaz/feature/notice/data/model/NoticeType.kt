package com.reyaz.feature.notice.data.model

sealed class NoticeType(
    val url: String,
    val typeId: String,
    val selector: String = ""
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

    data object NRI : NoticeType(
        url = "https://jmicoe.in/",
        typeId = "NRI"
    )
    data object Hostel : NoticeType(
        url = "https://jmi.ac.in/ACADEMICS/Hostels/University-Boys-Hostels/Notices",
        typeId = "HOSTEL",
        selector = "//div[contains(@class, 'post-desc') and contains(@class, 'tb_dott')]//a"
    )
}