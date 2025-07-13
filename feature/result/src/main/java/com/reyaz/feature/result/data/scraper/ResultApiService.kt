package com.reyaz.feature.result.data.scraper

import android.util.Log
import com.reyaz.core.network.utils.SSLTrustUtils.trustAllHosts
import com.reyaz.feature.result.data.local.dto.RemoteResultListDto
import com.reyaz.feature.result.data.mapper.ResultHtmlParser
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlPage
import org.htmlunit.html.HtmlSelect
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private const val TAG = "RESULT_API_SERVICE"

class ResultApiService(
    private val webClient: WebClient,
    private val parser: ResultHtmlParser
) {

    suspend fun fetchProgramTypes(): Result<List<CourseType>> = withContext(Dispatchers.IO) {
        try {
            clearWebClientState()
            val page: HtmlPage =
                webClient.getPage("https://admission.jmi.ac.in/EntranceResults/UniversityResult")
            webClient.waitForBackgroundJavaScript(5000)

            val dropdown = page.getElementByName<HtmlSelect>("frm_ProgramType")
            val result = dropdown.options
                .filter { it.valueAttribute.isNotEmpty() && it.valueAttribute != "selected" }
                .map { CourseType(id = it.valueAttribute, name = it.text.trim()) }

            Result.success(result)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading course types", e)
            Result.failure(e)
        }
    }

    suspend fun fetchPrograms(courseTypeValue: String): Result<List<CourseName>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                trustAllHosts()
                val url =
                    URL("https://admission.jmi.ac.in/EntranceResults/UniversityResult/getUniversityProgramName")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                val payload = "prgType=${URLEncoder.encode(courseTypeValue, "UTF-8")}"
                OutputStreamWriter(conn.outputStream).use { it.write(payload) }

                val result = mutableListOf<CourseName>()
                conn.inputStream.bufferedReader().use {
                    val response = it.readText()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        result.add(
                            CourseName(
                                id = obj.getString("CPD_ID"),
                                name = obj.getString("PROGNAME")
                            )
                        )
                    }
                }
                Result.success(result)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching program names", e)
                Result.failure(e)
            }
        }

    suspend fun fetchResult(
        courseTypeId: String,
        courseNameId: String,
        phdDisciplineId: String
    ): Result<List<RemoteResultListDto>> = withContext(Dispatchers.IO) {
        try {
            trustAllHosts()
            val url =
                URL("https://admission.jmi.ac.in/EntranceResults/UniversityResult/getUniversityResults")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            val payload = listOf(
                "frm_ProgramType" to courseTypeId,
                "frm_ProgramName" to courseNameId,
                "frm_PhDMainDiscipline" to phdDisciplineId
            ).joinToString("&") { (key, value) ->
                "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
            }   // like: frm_ProgramType=PHD&frm_ProgramName=PH1&frm_PhDMainDiscipline=M0015

            Log.d(TAG, "Payload: $payload")
            conn.outputStream.use { it.write(payload.toByteArray()) }

            val response = conn.inputStream.bufferedReader().readText()
            //Log.d(TAG, "RAW Response from remote for course_type: ${courseTypeId} and courseId: ${courseNameId}: $response")
            val jsonResponse = JSONObject(response)
            val htmlContentResponse = jsonResponse.getString("UniversityResults")

            val parsedResult = parser.parse(htmlContentResponse)
            Log.d(TAG, "Parsed result: $parsedResult")
            if (parsedResult.isSuccess) {
                Result.success(parsedResult.getOrDefault(emptyList()))
            } else {
                Result.failure(parsedResult.exceptionOrNull()!!)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error fetching results", e)
            Result.failure(e)
        }
    }

    private fun clearWebClientState() {
        try {
            webClient.cookieManager.clearCookies()
            webClient.cache.clear()
            webClient.currentWindow?.jobManager?.removeAllJobs()
            webClient.webWindows.forEach { it.jobManager.removeAllJobs() }
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing WebClient state: $e")
        }
    }

}
