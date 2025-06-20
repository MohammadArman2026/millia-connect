package com.reyaz.feature.notice.data.remote // Assuming "instea.instea" is a mistake

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.htmlunit.BrowserVersion
import org.htmlunit.NicelyResynchronizingAjaxController
import org.htmlunit.WaitingRefreshHandler
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlAnchor
import org.htmlunit.html.HtmlListItem
import org.htmlunit.html.HtmlPage
import org.htmlunit.html.HtmlUnorderedList
import java.net.URL

private const val TAG = "NOTICE_SCRAPER"

class NoticeScraper(
    private val webClient: WebClient
) {

    suspend fun scrapNotices(url: String = "https://jmi.ac.in/ACADEMICS/Academic-Calendar/Academic-Calendar"/*, selector: String, type: String*/) =
        withContext(Dispatchers.IO) {
            //Log.d(TAG, "scrapNotices: $url")
            try {
                val page: HtmlPage = webClient.getPage(url)
                webClient.waitForBackgroundJavaScript(3000)

                val anchor = page.getFirstByXPath<HtmlAnchor>(
                    "//div[contains(@class, 'bg_gray')]//ul[contains(@class, 'unorder-list')]//li[1]//a"
                )
//                val anchor = firstLi?.getFirstByXPath<HtmlAnchor>(".//a")

                if (anchor != null) {
                    val title = anchor.textContent.trim()
                    val href = anchor.hrefAttribute.trim()
                    val fullUrl = URL(page.baseURL, href).toString()

                    Log.d(TAG, "Title: $title")
                    Log.d(TAG, "URL: $fullUrl")
                } else {
                    //todo: open web-view
                }

                /* val ul =
                     page.getFirstByXPath<HtmlUnorderedList>("//div[contains(@class, 'bg_gray')]//ul[contains(@class, 'unorder-list')]")

                 if (ul != null) {
                     val listItems = ul.getByXPath<HtmlListItem>(".//li")

                     listItems.forEach { item ->
                         val anchor = item.getFirstByXPath<HtmlAnchor>(".//a")
                         if (anchor != null) {
                             val title = anchor.textContent.trim()
                             val href = anchor.hrefAttribute.trim()
                             Log.d(TAG, "$title — https://jmi.ac.in$href")
                         }
                     }
                 } else {
 //                    todo: open web-view
                 }*/

            } catch (e: Exception) {
                Log.d("NoticeRepository", "Error While fetching notices", e)
//                emptyList<NoticeModal>() // Returning empty list with logging
            } finally {
                webClient.close()
            }
        }
}