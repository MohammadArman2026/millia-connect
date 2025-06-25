package com.reyaz.feature.notice.data.remote // Assuming "instea.instea" is a mistake

import android.util.Log
import com.reyaz.feature.notice.data.model.NoticeDto
import com.reyaz.feature.notice.data.model.NoticeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlPage

private const val TAG = "NOTICE_SCRAPER"

class NoticeScraper(
    private val webClient: WebClient,
    private val parser: NoticeParser
) {

    suspend fun scrapNotices(noticeType: NoticeType): Result<List<NoticeDto>> =
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching notices for $noticeType from ${noticeType.url}")
                val page: HtmlPage = fetchPage(noticeType.url)

                //Log.d(TAG, "Fetched page length: ${page.asNormalizedText().length}")
                val parseResult: Result<List<NoticeDto>> =
                    when (noticeType) {
//                        NoticeType.AcademicCalendar -> parser.parseAcademicCalendar(page)
                        NoticeType.AcademicCalendar -> {
                            val enggCal = parser.parseAnchorsByPath1(
                                page = fetchPage("https://jmi.ac.in/ACADEMICS/Academic-Calendar/Academic-Calendar-F/O-Engg.-And-Tech."),
                                xPath = "//div[contains(@class, 'gray-bg')]//a",
                                noticeType = NoticeType.AcademicCalendar,
                                limit = 2,
                            )
                            val uniCal = parser.parseAnchorsByPath1(
                                page = page,
                                xPath = "//div[contains(@class, 'bg_gray')]//a",
                                noticeType = NoticeType.AcademicCalendar,
                                limit = 2
                            )
                            val disCal = parser.parseAnchorsByPath1(
                                page = fetchPage("https://jmi.ac.in/Centre-For-Distance-And-Online-Education-(CDOE)/Academic-Calendar"),
                                xPath = "//div[contains(@class, 'gray-bg')]//a",
                                noticeType = NoticeType.AcademicCalendar,
                                limit = 2
                            )
                            val dentistryCal = parser.parseAnchorsByPath1(
                                page = fetchPage("https://jmi.ac.in/ACADEMICS/Academic-Calendar/Academic-Calendar-F/O-Dentistry"),
                                xPath = "//div[contains(@class, 'bg_gray')]//a",
                                noticeType = NoticeType.AcademicCalendar,
                                limit = 2
                            )
                            Result.success((enggCal.getOrNull() ?: emptyList()) + (dentistryCal.getOrNull() ?: emptyList()) + (uniCal.getOrNull() ?: emptyList())+ (disCal.getOrNull() ?: emptyList()))
                        }
                        NoticeType.Holiday -> parser.parseHoliday(page)
                        NoticeType.Admission -> {
                            val newSiteNotices = parser.parseAdmissionNotices(page, noticeType)

                            val oldPage = fetchPage("https://jmicoe.in/")
                            val oldSiteNotices = parser.parseAnchorsByPath(oldPage, noticeType, "//*[@id='leftPanel']")

                            Result.success((newSiteNotices.getOrNull() ?: emptyList()) + (oldSiteNotices.getOrNull() ?: emptyList()))
                        }
                        NoticeType.Examination,
                        NoticeType.General,
                        NoticeType.Academics -> parser.parseAdmissionNotices(page, noticeType)
                        NoticeType.Urgent -> parser.parseUrgentNotices(page)
                        NoticeType.NRI -> parser.parseAnchorsByPath(page, noticeType, "//*[@id='2ndrightPanel']")
                        NoticeType.Hostel -> {
                            // boys hostel
                            val boysHostelNotices = parser.parseAnchorsByPath1(page, noticeType, xPath = noticeType.selector, limit = 4)
                            val girlsHostelNotices = parser.parseAnchorsByPath1(
                                page = fetchPage("https://jmi.ac.in/ACADEMICS/Hostels/University-Girls-Hostels/Notices"),
                                noticeType = noticeType,
                                noticeType.selector,
                                limit = 4
                            )
                            Result.success((boysHostelNotices.getOrNull() ?: emptyList()) + (girlsHostelNotices.getOrNull() ?: emptyList()))
                        }
                    }
                parseResult
            } catch (e: Exception) {
                Log.d(TAG, "Error While fetching notices", e)
                Result.failure(Exception("Fetching failed"))
            }
        }

    private suspend fun fetchPage(url: String): HtmlPage {
        return webClient.getPage<HtmlPage>(url).apply {
            webClient.waitForBackgroundJavaScript(3000)
//            Log.d(TAG, "Fetched page: $url")
        }
    }
}

