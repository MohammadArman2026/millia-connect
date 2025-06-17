package com.reyaz.feature.result.data.mapper

import android.util.Log
import com.reyaz.feature.result.data.local.dto.RemoteResultListDto
import org.jsoup.Jsoup

private const val TAG = "RESULT_HTML_PARSER"
class ResultHtmlParser {
    fun parse(htmlContent: String): Result<List<RemoteResultListDto>> {
        try {
            val document = Jsoup.parse(htmlContent)
            val rows = document.select("table tr").drop(1) // skip header or rather we can check if fetched table got the correct header
            val results = mutableListOf<RemoteResultListDto>()

            for (row in rows) {
                val cells = row.select("td")    // cells is a list of <td> elements (i.e., individual table cells) inside a single <tr> row.
                if (cells.size >= 5) {
                    results.add(
                        RemoteResultListDto(
                            srNo = cells[0].text().trim(),
                            courseName = cells[1].text().trim(),
                            date = cells[2].text().trim(),
                            remark = cells[3].text().trim(),
                            link = row.selectFirst("a[href]")?.attr("href")?.trim()
                        )
                    )
                }
            }

            if (results.isEmpty()) {
                Log.d(TAG, "No rows parsed.")
            }
            return Result.success(results)

        } catch (e: Exception) {
            Log.d(TAG, "Error parsing HTML: ${e.message}")
            return Result.failure(e)
        }
    }
}
