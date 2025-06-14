package com.reyaz.feature.result.data;

import android.util.Log;

import com.reyaz.feature.result.domain.model.CourseName;
import com.reyaz.feature.result.domain.model.CourseType;

import org.htmlunit.BrowserVersion;
import org.htmlunit.NicelyResynchronizingAjaxController;
import org.htmlunit.WaitingRefreshHandler;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlOption;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSelect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DropdownSelector {
    private static final String TAG = "RESULT_SCRAPER_JAVA";

    private final WebClient webClient;

    public DropdownSelector() {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.setRefreshHandler(new WaitingRefreshHandler());

        // Uncomment to allow all SSL certificates
        // configureSslContext(webClient);
    }

    public Callable<Result<List<CourseType>>> fetchProgram() {
        return () -> {
            String url = "https://admission.jmi.ac.in/EntranceResults/UniversityResult";
            try {
                HtmlPage page = webClient.getPage(url);
                webClient.waitForBackgroundJavaScript(5000);

                HtmlSelect programTypeSelect = (HtmlSelect) page.getElementByName("frm_ProgramType");
                List<HtmlOption> options = programTypeSelect.getOptions();
                List<CourseType> courseTypes = new ArrayList<>();

                for (HtmlOption option : options) {
                    String value = option.getValueAttribute();
                    if (!value.isEmpty() && !"selected".equals(value)) {
                        courseTypes.add(new CourseType(value, option.getTextContent().trim()));
                    }
                }

                Log.d(TAG, "Found " + courseTypes.size() + " course types");
                return Result.success(courseTypes);
            } catch (Exception e) {
                Log.e(TAG, "Error fetching course types: " + e.getMessage());
                return Result.failure(e);
            }
        };
    }

    public static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String courseTypeValue = "UG1";
    public void getProgramsForCourseType() {
        String url = "https://admission.jmi.ac.in/EntranceResults/UniversityResult";
        try {
            Log.d(TAG, "JAVA Fetching programs for course type: " + courseTypeValue);
            HtmlPage page = webClient.getPage(url);
            webClient.waitForBackgroundJavaScript(5000);

            //Log.d(TAG, "page:  " + page.asNormalizedText());
            HtmlSelect programTypeSelect = page.getElementByName("frm_ProgramType");
            for (HtmlOption option : programTypeSelect.getOptions()) {
                String value = option.getValueAttribute();
                if (!value.isEmpty() && !"selected".equals(value)) {
                    Log.d(TAG, "Program Type:  " + option.getValueAttribute());
                }
            }
            HtmlOption option = programTypeSelect.getOptionByValue(courseTypeValue);
            programTypeSelect.setSelectedAttribute(option, true);

            programTypeSelect.fireEvent("change");
            webClient.waitForBackgroundJavaScript(10000);
            Log.d(TAG, "Program Type changed");

            HtmlSelect programNameSelect = page.getElementByName("frm_ProgramName");
            List<HtmlOption> options = programNameSelect.getOptions();

            List<CourseName> programs = new ArrayList<>();

            for (HtmlOption progOption : options) {
                String value = progOption.getValueAttribute();
                String text = progOption.getTextContent().trim();
                if (!value.isEmpty() && !text.contains("Select Program Name")) {
                    Log.d(TAG, "Program: " + value + " (" + text + ")");
                    programs.add(new CourseName(value, text));
                }
            }

            Log.d(TAG, "Found " + programs.size() + " programs for course type: " + courseTypeValue);
            //return Result.success(programs);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching programs: " + e);
            //return Result.failure(e);
        }
    }

    public List<CourseName> fetchProgByHardCode() {
        List<CourseName> programs = new ArrayList<>();
        try {
            Log.d(TAG, "JAVA Hardcode");
            String endpoint = "https://admission.jmi.ac.in/EntranceResults/UniversityResult/getUniversityProgramName"; // Replace with actual XHR endpoint
            trustAllHosts();
            HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String payload = "prgType=" + URLEncoder.encode(courseTypeValue, "UTF-8");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Log.d(TAG, "Line : "+line);
                    // Assuming server returns raw HTML <option> elements
                    Pattern pattern = Pattern.compile("<option value=\"(.*?)\">(.*?)</option>");
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        String value = matcher.group(1);
                        String text = matcher.group(2);
                        if (!value.isEmpty() && !text.contains("Select Program Name")) {
                            programs.add(new CourseName(value, text));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching programs: " + e);
            e.printStackTrace();
        }
        for (CourseName program :programs) {
            Log.d(TAG, program.toString());
        }
        return programs;
    }

    // Simple result wrapper like Kotlin's Result
    public static class Result<T> {
        private final T data;
        private final Throwable error;

        private Result(T data, Throwable error) {
            this.data = data;
            this.error = error;
        }

        public static <T> Result<T> success(T data) {
            return new Result<>(data, null);
        }

        public static <T> Result<T> failure(Throwable error) {
            return new Result<>(null, error);
        }

        public boolean isSuccess() {
            return error == null;
        }

        public T getData() {
            return data;
        }

        public Throwable getError() {
            return error;
        }
    }
}