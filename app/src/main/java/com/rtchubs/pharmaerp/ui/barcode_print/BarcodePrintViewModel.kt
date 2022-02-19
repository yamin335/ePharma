package com.rtchubs.pharmaerp.ui.barcode_print

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.repos.StockProductRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlin.math.abs

class BarcodePrintViewModel @Inject constructor(
    private val application: Application,
    private val stockProductRepository: StockProductRepository
) : BaseViewModel(application) {

    val showHideProgress: MutableLiveData<Pair<Boolean, Int>> by lazy {
        MutableLiveData<Pair<Boolean, Int>>()
    }

    val pdfDownloadResponse: MutableLiveData<Pair<Boolean, String>> by lazy {
        MutableLiveData<Pair<Boolean, String>>()
    }

    fun downloadPdfFile(downloadUrl: String, filePath: String, fileName: String) {
        if (checkNetworkStatus()) {
            showHideProgress.postValue(Pair(true, 0))
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                showHideProgress.postValue(Pair(false, 100))
                pdfDownloadResponse.postValue(Pair(false, ""))
            }

            viewModelScope.launch(handler) {
                val response = downloadFile(downloadUrl, filePath, fileName)
                if (response == null) {
                    showHideProgress.postValue(Pair(false, 100))
                    pdfDownloadResponse.postValue(Pair(false, ""))
                } else {
                    pdfDownloadResponse.postValue(Pair(true, "${response.first}/${response.second}"))
                }
            }
        }
    }

    private suspend fun downloadFile(downloadUrl: String, filePath: String, fileName: String): Pair<String, String>? {
        return withContext(Dispatchers.IO) {
            // Normally we would do some work here, like download a file.
            try {
                var currentProgress: Int
                var downloadedSize = 0
                val urlConnection: HttpURLConnection
                val url = URL(downloadUrl)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                //urlConnection.doOutput = true
                urlConnection.connect()

                val downloadedFile = File(
                    filePath,
                    fileName
                )
                if (!downloadedFile.exists()) {
                    downloadedFile.createNewFile()
                }
                val inputStream: InputStream = urlConnection.inputStream
                val fileOutputStream = FileOutputStream(downloadedFile)
                val totalSize = urlConnection.contentLength
                val buffer = ByteArray(2024)
                var bufferLength: Int
                fileOutputStream.use { outputStream ->
                    inputStream.use { inStream ->
                        while (inStream.read(buffer).also { bufferLength = it } > 0) {
                            outputStream.write(buffer, 0, bufferLength)
                            downloadedSize += bufferLength
                            currentProgress = abs(downloadedSize * 100 / totalSize)
                            showHideProgress.postValue(Pair(true, currentProgress))
                        }
                    }
                }
                Pair(filePath, fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}