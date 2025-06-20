package com.reyaz.core.network.utils

import org.htmlunit.ScriptException
import org.htmlunit.html.HtmlPage
import org.htmlunit.javascript.JavaScriptErrorListener
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

class NoOpJavaScriptErrorListener : JavaScriptErrorListener {
    override fun scriptException(page: HtmlPage?, scriptException: ScriptException?) {
        // Do nothing to suppress the log
    }

    override fun timeoutError(page: HtmlPage?, allowedTime: Long, executionTime: Long) {
        // Do nothing to suppress the log
    }

    override fun malformedScriptURL(p0: HtmlPage?, p1: String?, p2: MalformedURLException?) {
    }

    override fun loadScriptError(p0: HtmlPage?, p1: URL?, p2: Exception?) {
    }

    override fun warn(p0: String?, p1: String?, p2: Int, p3: String?, p4: Int) {
    }


}