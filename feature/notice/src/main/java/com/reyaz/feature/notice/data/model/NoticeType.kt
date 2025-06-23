package com.reyaz.feature.notice.data.model

sealed class NoticeType(
    val url: String,
    val typeId: String,
    //val selector: String
) {
    data object AcademicCalendar : NoticeType(
        url = "https://jmi.ac.in/ACADEMICS/Academic-Calendar/Academic-Calendar",
        //selector = "//div[contains(@class, 'bg_gray')]//ul[contains(@class, 'unorder-list')]//li[1]//a",
        typeId = "CAL"
    )

    data object Holiday : NoticeType(
        url = "https://jmi.ac.in/ACADEMICS/Academic-Calendar/List-Of-Holidays",
        //selector = "//div[contains(@class, 'bg_gray')]//ul[contains(@class, 'unorder-list')]//li[1]//a",
        typeId = "HOL"
    )
}