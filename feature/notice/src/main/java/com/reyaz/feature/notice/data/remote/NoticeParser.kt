package com.reyaz.feature.notice.data.remote

import android.util.Log
import com.reyaz.feature.notice.data.model.NoticeDto
import com.reyaz.feature.notice.data.model.NoticeType
import kotlinx.coroutines.delay
import org.htmlunit.html.HtmlAnchor
import org.htmlunit.html.HtmlDivision
import org.htmlunit.html.HtmlListItem
import org.htmlunit.html.HtmlPage
import org.htmlunit.html.HtmlUnorderedList
import java.net.URL

private const val TAG = "NOTICE_PARSER"

class NoticeParser {

    fun parseAcademicCalendar(page: HtmlPage): Result<List<NoticeDto>> {
        try {
            Log.d(TAG, "Parsing academic calendar..")
            val ul = page.getFirstByXPath<HtmlUnorderedList>(
                "//div[contains(@class, 'bg_gray')]//ul[contains(@class, 'unorder-list')]"
            )

            if (ul != null) {
                val listItems = ul.getByXPath<HtmlListItem>(".//li")
                val acadList: List<NoticeDto> = listItems.take(3).mapNotNull { item ->
                    val anchor = item.getFirstByXPath<HtmlAnchor>(".//a")
                    if (anchor != null) {
                        val title = anchor.textContent.trim()
                        val href = anchor.hrefAttribute.trim()
                        val fullUrl = URL(page.baseURL, href).toString()
                        Log.d(TAG, "$title — $fullUrl")
                        NoticeDto(title = title, url = fullUrl, NoticeType.AcademicCalendar)
                    } else {
                        null
                    }
                }
                return Result.success(acadList)
            } else {
                throw Exception("Tag not found while parsing")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while parsing notice", e)
            return Result.failure(Exception("Error while parsing the academic calender"))
        }
    }

    suspend fun parseHoliday(page: HtmlPage): Result<List<NoticeDto>> {
        try {
            Log.d(TAG, "Parsing holidays..")
            val div = page.getFirstByXPath<HtmlDivision>(
                "//div[contains(@class, 'bg_gray')]"
            )

            val notices = mutableListOf<NoticeDto>()

            if (div != null) {
                val anchors = div.getByXPath<HtmlAnchor>(".//a")

                anchors.take(5).forEach { anchor ->
                    val title = anchor.textContent.trim()
                    val href = anchor.hrefAttribute.trim()
                    val fullUrl = URL(page.baseURL, href).toString()
                    delay(500)
                    notices.add(NoticeDto(title = title, type = NoticeType.Holiday, url = fullUrl, ))
                    Log.d("NOTICE", "$title — $fullUrl")
                }
                return Result.success(notices)
            } else {
                throw Exception("Tag not found while parsing")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while parsing notice", e)
            return Result.failure(Exception("Error while parsing the academic calender"))
        }
    }
}