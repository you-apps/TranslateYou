/*
 * Copyright (c) 2023 You Apps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bnyro.translate.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bnyro.translate.R
import com.bnyro.translate.api.ExternalApi
import com.bnyro.translate.ext.toastFromMainThread
import com.bnyro.translate.obj.TessLanguage
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File

object TessHelper {
    private const val githubApiUrl = "https://api.github.com"
    const val tessTreePath = "/repos/tesseract-ocr/tessdata/git/trees/main"
    private const val baseUrl = "https://raw.githubusercontent.com/tesseract-ocr/tessdata/main"
    const val tessRepoUrl = "https://github.com/tesseract-ocr/tessdata"
    const val DATA_FILE_SUFFIX = ".traineddata"
    private const val TESS_DIR = "tessdata"

    private const val DOWNLOAD_BUFFER_SIZE = 200 * 1024 // 200kB

    private val externalApi by lazy {
        RetrofitHelper.createInstance<ExternalApi>(githubApiUrl)
    }

    suspend fun getAvailableLanguages(): List<TessLanguage> {
        return try {
            externalApi.getAvailableTessLanguages().tree
                .filter { it.path.endsWith(DATA_FILE_SUFFIX) }
        } catch (e: Exception) {
            Log.e("fetching tess languages", e.toString())
            return emptyList()
        }
    }

    fun getText(context: Context, bitmap: Bitmap): String? {
        val tess = TessBaseAPI()
        val rootDir = getRootDir(context)
        val language = Preferences.get(Preferences.tessLanguageKey, "eng")

        try {
            if (!tess.init(rootDir.absolutePath, language)) {
                context.toastFromMainThread(R.string.select_language)
                return null
            }
            tess.setImage(bitmap)

            return tess.utF8Text
        } finally {
            tess.recycle()
        }
    }

    suspend fun downloadLanguageData(context: Context, languagePath: String, onProgress: (Float) -> Unit) {
        val url = "$baseUrl/$languagePath"
        val targetFile = File(getTessDir(context), languagePath)

        val connection = externalApi.downloadTessLanguageData(url)
        val fullSize = connection.contentLength()
        var currentDownloadSize = 0L

        try {
            targetFile.outputStream().use { fileOutputStream ->
                connection.byteStream().use { inputStream ->
                    val buffer = ByteArray(DOWNLOAD_BUFFER_SIZE)
                    var bytesRead = 0

                    while (bytesRead >= 0) {
                        bytesRead = inputStream.read(buffer)
                        fileOutputStream.write(bytesRead)

                        currentDownloadSize += bytesRead
                        onProgress(currentDownloadSize.toFloat() / fullSize)
                    }
                    onProgress(1f)
                }
            }
        } catch (_: Exception) {
            onProgress.invoke(-1f)
            targetFile.delete()
        }
    }

    fun getDownloadedLanguages(context: Context): List<String> {
        return getTessDir(context).listFiles().orEmpty().map {
            it.nameWithoutExtension
        }
    }

    fun deleteLanguage(context: Context, language: String): Boolean {
        val file = File(getTessDir(context), "$language$DATA_FILE_SUFFIX")
        return if (file.exists()) file.delete() else false
    }

    fun areLanguagesDownloaded(context: Context): Boolean {
        return getTessDir(context).listFiles()?.isNotEmpty() ?: false
    }

    private fun getRootDir(context: Context) = context.getExternalFilesDir(null)!!

    private fun getTessDir(context: Context) = File(getRootDir(context), TESS_DIR).also {
        if (!it.exists()) it.mkdirs()
    }
}
