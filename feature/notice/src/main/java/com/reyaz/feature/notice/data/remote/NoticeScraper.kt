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
                //Log.d(TAG, "Fetching notices for $noticeType")
                val page: HtmlPage = webClient.getPage(noticeType.url)
                webClient.waitForBackgroundJavaScript(3000)

                //Log.d(TAG, "Fetched page length: ${page.asNormalizedText().length}")
                val parseResult: Result<List<NoticeDto>> =
                    when (noticeType) {
                        NoticeType.AcademicCalendar -> parser.parseAcademicCalendar(page)
                        NoticeType.Holiday -> parser.parseHoliday(page)
                        NoticeType.Admission -> parser.parseAdmissionNotices(page, noticeType)
                        NoticeType.Examination -> parser.parseAdmissionNotices(page, noticeType)
                        NoticeType.General -> parser.parseAdmissionNotices(page, noticeType)
                        NoticeType.Academics -> parser.parseAdmissionNotices(page, noticeType)
                        NoticeType.Urgent -> parser.parseUrgentNotices(page)
                    }
                parseResult
            } catch (e: Exception) {
                Log.d(TAG, "Error While fetching notices", e)
                Result.failure(Exception("Fetching failed"))
            }
        }
}

