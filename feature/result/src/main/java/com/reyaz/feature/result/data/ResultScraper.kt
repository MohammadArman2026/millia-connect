package com.reyaz.feature.result.data

import android.util.Log
import com.reyaz.feature.result.data.local.dto.RemoteCourseResultDto
import com.reyaz.feature.result.data.local.dto.RemoteResultListDto
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.htmlunit.BrowserVersion
import org.htmlunit.NicelyResynchronizingAjaxController
import org.htmlunit.WaitingRefreshHandler
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlPage
import org.htmlunit.html.HtmlSelect
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private const val TAG = "RESULT_SCRAPER"

class ResultScraper(
    private val dropdownSelector: DropdownSelector
) {

    val webClient = WebClient(BrowserVersion.CHROME).apply {
        options.isJavaScriptEnabled = true
        options.isCssEnabled = false
        options.isThrowExceptionOnScriptError = false
        options.isThrowExceptionOnFailingStatusCode = false
        options.isPrintContentOnFailingStatusCode = false
        options.isUseInsecureSSL = true  // This allows insecure SSL
        ajaxController = NicelyResynchronizingAjaxController()
        refreshHandler = WaitingRefreshHandler()

        // Configure SSL to accept all certificates
        //configureSslContext()
    }

    private fun WebClient.configureSslContext() {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            this.options.sslClientCertificateStore
            this.options.sslInsecureProtocol = "TLSv1.2"
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring SSL context: $e")
        }
    }


    suspend fun fetchProgramTypes(): Result<List<CourseType>> = withContext(Dispatchers.IO) {
        val url = "https://admission.jmi.ac.in/EntranceResults/UniversityResult"
        try {
            val page: HtmlPage = webClient.getPage(url)
            webClient.waitForBackgroundJavaScript(5000)

            // Find the Program Type dropdown
            val programTypeSelect = page.getElementByName<HtmlSelect>("frm_ProgramType")

            val courseTypeTypes = programTypeSelect.options
                .filter { it.valueAttribute.isNotEmpty() && it.valueAttribute != "selected" }
                .map { option ->
                    CourseType(
                        id = option.valueAttribute,
                        name = option.text.trim()
                    )
                }

            // Log.d(TAG, "Found ${courseTypeTypes.size} course types")
            // courseTypes.forEach { Log.d(TAG, "Course Type: ${it.text} (${it.value})") }

            Result.success(courseTypeTypes)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching course types: $e")
            Result.failure(e)
        }
    }

    /**
     * Get programs for a specific course type
     */
    @Deprecated("Cannot execute js")
    suspend fun getProgramsForCourseType(courseTypeValue: String = "UG1"): Result<List<CourseName>> =
        withContext(Dispatchers.IO) {
            dropdownSelector.getProgramsForCourseType()
            val url = "https://admission.jmi.ac.in/EntranceResults/UniversityResult"
            try {
                // Log.d(TAG, "Fetching programs for course type: $courseTypeValue")

                val page: HtmlPage = webClient.getPage(url)
                webClient.waitForBackgroundJavaScript(5000)

                // Find and select the Program Type dropdown
                val programTypeSelect = page.getElementByName<HtmlSelect>("frm_ProgramType")
                val programTypeOption = programTypeSelect.getOptionByValue(courseTypeValue)
                //programTypeSelect.setSelectedAttribute(programTypeOption, true)

                // Trigger the change event to populate Program Name dropdown
                programTypeSelect.fireEvent("change")

                // Wait for AJAX to complete
                webClient.waitForBackgroundJavaScript(10000)

                // Now get the Program Name dropdown
                val programNameSelect = page.getElementByName<HtmlSelect>("frm_ProgramName")

                val programs = programNameSelect.options
                    .filter { it.valueAttribute.isNotEmpty() && !it.text.contains("Select Program Name") }
                    .map { option ->
                        CourseName(
                            id = option.valueAttribute,
                            name = option.text.trim()
                        )
                    }

                // Log.d(TAG, "Found ${programs.size} programs for course type: $courseTypeValue")
                //  programs.forEach { Log.d(TAG, "Program: ${it.id} (${it.name})") }

//            ScrapingResult(programs = programs)
                Result.success(programs)

            } catch (e: Exception) {
                Log.e(TAG, "Error fetching programs: $e")
                Result.failure(e)
//            ScrapingResult(error = e.message)
            }
        }

    private fun trustAllHosts() {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            }
        )

        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun fetchPrograms(courseTypeValue: String): Result<List<CourseName>> {
        val programs = mutableListOf<CourseName>()

        try {
            // Log.d("RESULT_SCRAPER", "Kotlin Hardcode")
            val endpoint =
                "https://admission.jmi.ac.in/EntranceResults/UniversityResult/getUniversityProgramName"

            val url = URL(endpoint)
            trustAllHosts()     // call this before opening the connection
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            val payload = "prgType=${URLEncoder.encode(courseTypeValue, "UTF-8")}"
            conn.outputStream.use { os ->
                os.write(payload.toByteArray())
            }

            conn.inputStream.bufferedReader().use { reader ->
                val response = reader.readText()
                // Log.d("RESULT_SCRAPER", "Response: $response")

                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val id = obj.getString("CPD_ID")
                    val name = obj.getString("PROGNAME")
                    programs.add(CourseName(id, name))
                }
            }
        } catch (e: Exception) {
            Log.e("RESULT_SCRAPER", "Error fetching programs: ${e.message}", e)
            //Result.failure(e)
        }

        /*for (program in programs) {
             Log.d("RESULT_SCRAPER", program.toString())
        }*/

        return Result.success(programs)
    }

    fun fetchResult(
        courseTypeId: String,
        courseNameId: String,
        phdDisciplineId: String = ""
    ): Result<List<RemoteResultListDto>> {

        //val programs = mutableListOf<RemoteCourseResultDto>()

        return try {
            Log.d("RESULT_SCRAPER", "Result fetching...")

            val payload = listOf(
                "frm_ProgramType" to courseTypeId,
                "frm_ProgramName" to courseNameId,
                "frm_PhDMainDiscipline" to phdDisciplineId
            ).joinToString("&") { (key, value) ->
                "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
            }   // like: frm_ProgramType=PHD&frm_ProgramName=PH1&frm_PhDMainDiscipline=M0015

            // Log.d("RESULT_SCRAPER", "Payload created: $payload")

            val endpoint =
                "https://admission.jmi.ac.in/EntranceResults/UniversityResult/getUniversityResults"

            val url = URL(endpoint)
            trustAllHosts()     // call this before opening the connection
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            // Log.d("RESULT_SCRAPER", "Payload sening...: $payload")
            conn.outputStream.use { os ->
                os.write(payload.toByteArray())
            }
            // Log.d("RESULT_SCRAPER", "Payload sent")

            conn.inputStream.bufferedReader().use { reader ->
                val response = reader.readText()
                // Log.d("RESULT_SCRAPER", "Response: $response")
                val jsonResponse = JSONObject(response)
                val htmlContent = jsonResponse.getString("UniversityResults")
                val result = parseUniversityResultsTable(htmlContent = htmlContent)

                for (program in result) {
                    Log.d("RESULT_SCRAPER", program.toString())
                }

                Result.success(result)
            }
        } catch (e: Exception) {
            Log.e("RESULT_SCRAPER", "Error fetching programs: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun parseUniversityResultsTable(htmlContent: String): List<RemoteResultListDto> {
        return try {
            Log.d("RESULT_SCRAPER", "Parsing...")
            val results = mutableListOf<RemoteResultListDto>()

            val document: org.jsoup.nodes.Document = Jsoup.parse(htmlContent)
            val rows = document.select("table tr")

            if (rows.isEmpty())
                Log.d("RESULT_SCRAPER", "No list Released....")
            for (row in rows.drop(1)) {     // skip the header
                val cells =
                    row.select("td")    // cells is a list of <td> elements (i.e., individual table cells) inside a single <tr> row.
                Log.d("RESULT_SCRAPER", "Row: $cells")

                if (cells.size >= 5) {
                    val srNo = cells[0].text().trim()
                    val courseName = cells[1].text().trim()
                    val date = cells[2].text().trim()
                    val remarks = cells[3].text().trim()
                    val pdfUrl = row.selectFirst("a[href]")?.attr("href")?.trim()

                    Log.d("RESULT_SCRAPER", "Parsed: $courseName | $date | $remarks | $pdfUrl")

                    results.add(
                        RemoteResultListDto(
                            srNo = srNo,
                            courseName = courseName,
                            date = date,
                            remark = remarks,
                            link = pdfUrl
                        )
                    )
                }
            }

            results
        } catch (e: Exception) {
            Log.d(TAG, "Error parsing: ${e.message}")
            emptyList()
        }
    }
}
