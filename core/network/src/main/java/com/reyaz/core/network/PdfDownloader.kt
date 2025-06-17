package com.reyaz.core.network

import android.content.Context
import android.util.Log
import com.reyaz.core.network.model.DownloadResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "PDF_DOWNLOADER"
class PdfDownloader(
    private val context: Context
)  {

    fun downloadPdf(url: String, fileName: String): Flow<DownloadResult> = flow {
        withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                val urlConnection = URL(url)
                connection = urlConnection.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    throw IOException("Server returned HTTP ${connection.responseCode}")
                }

                val fileSize = connection.contentLength
                val input = BufferedInputStream(connection.inputStream)
                val file = File(context.filesDir, fileName)

                if (file.exists()) {
                    Log.d(TAG, "File '$fileName' already exists. Attempting to replace.")
                    if (!file.delete()) {
                        // If deletion fails, it's a critical error as we can't replace the file.
                        val errorMessage = "Failed to delete existing file: $fileName"
                        Log.e(TAG, errorMessage)
                        emit(DownloadResult.Error(IOException(errorMessage)))
                        return@withContext // Exit the flow if we can't delete
                    } else {
                        Log.d(TAG, "Existing file '$fileName' successfully deleted.")
                    }
                }

                val output = FileOutputStream(file)
                val data = ByteArray(8192)
                var total: Long = 0
                var count: Int
                var lastProgress = 0

                while (input.read(data).also { count = it } != -1) {
                    total += count
                    output.write(data, 0, count)

                    // Calculate progress based on total bytes read if fileSize is valid
                    if (fileSize > 0) { // Avoid division by zero if fileSize is unknown/invalid
                        val progress = ((total * 100) / fileSize).toInt()
                        if (progress != lastProgress) {
                            emit(DownloadResult.Progress(progress))
                            lastProgress = progress
                        }
                    } else {
                        // If file size is unknown, emit progress differently or not at all
                        emit(DownloadResult.Progress(null))
                    }
                }
                output.flush()
                output.close()
                input.close()

                emit(DownloadResult.Success(file.absolutePath))
            } catch (e: Exception) {
                emit(DownloadResult.Error(e))
            } finally {
                connection?.disconnect()
            }
        }
    }.flowOn(Dispatchers.IO)
}
