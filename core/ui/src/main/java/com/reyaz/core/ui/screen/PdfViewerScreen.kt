package com.reyaz.core.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rajat.pdfviewer.util.PdfSource
import java.io.File

@Composable
fun PdfViewerScreen(filePath: String) {
    PdfRendererViewCompose(
        source = PdfSource.LocalFile(File(filePath)),
        modifier = Modifier.fillMaxSize(),
    )
}
