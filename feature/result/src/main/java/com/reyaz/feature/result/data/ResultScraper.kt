package com.reyaz.feature.result.data

import android.util.Log
import com.reyaz.feature.result.domain.model.CourseName
import com.reyaz.feature.result.domain.model.CourseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.htmlunit.BrowserVersion
import org.htmlunit.NicelyResynchronizingAjaxController
import org.htmlunit.WaitingRefreshHandler
import org.htmlunit.WebClient
import org.htmlunit.html.HtmlOption
import org.htmlunit.html.HtmlPage
import org.htmlunit.html.HtmlSelect
import java.security.cert.X509Certificate
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


    suspend fun fetchProgram(): Result<List<CourseType>> = withContext(Dispatchers.IO) {
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

            Log.d(TAG, "Found ${courseTypeTypes.size} course types")
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
    suspend fun getProgramsForCourseType(courseTypeValue: String = "UG1"): Result<List<CourseName>> =
        withContext(Dispatchers.IO) {
            dropdownSelector.getProgramsForCourseType()
            val url = "https://admission.jmi.ac.in/EntranceResults/UniversityResult"
            try {
                Log.d(TAG, "Fetching programs for course type: $courseTypeValue")

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

                Log.d(TAG, "Found ${programs.size} programs for course type: $courseTypeValue")
                programs.forEach { Log.d(TAG, "Program: ${it.id} (${it.name})") }

//            ScrapingResult(programs = programs)
                Result.success(programs)

            } catch (e: Exception) {
                Log.e(TAG, "Error fetching programs: $e")
                Result.failure(e)
//            ScrapingResult(error = e.message)
            }
        }
}
