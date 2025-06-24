package com.reyaz.core.network

import com.reyaz.core.network.utils.NoOpJavaScriptErrorListener
import org.htmlunit.BrowserVersion
import org.htmlunit.NicelyResynchronizingAjaxController
import org.htmlunit.WaitingRefreshHandler
import org.htmlunit.WebClient
import org.koin.dsl.module

val networkModule  = module{
    single { PdfManager(context = get()) }

    single {
        WebClient(BrowserVersion.CHROME).apply {
            options.isJavaScriptEnabled = true
            options.isRedirectEnabled = true
            options.isCssEnabled = false
            options.isThrowExceptionOnScriptError = false
            options.isThrowExceptionOnFailingStatusCode = false
            options.isPrintContentOnFailingStatusCode = false
            options.isUseInsecureSSL = true  // This allows insecure SSL
            ajaxController = NicelyResynchronizingAjaxController()
            refreshHandler = WaitingRefreshHandler()
            javaScriptErrorListener = NoOpJavaScriptErrorListener() // catching the error to avoid logging into logcat
        }
    }
}