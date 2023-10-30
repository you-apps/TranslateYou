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

import android.app.DownloadManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
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

    fun getText(context: Context, uri: Uri?): String? {
        val tess = TessBaseAPI()

        uri ?: return null

        val language = Preferences.get(Preferences.tessLanguageKey, "eng")
        val rootDir = getRootDir(context)

        if (!tess.init(rootDir.absolutePath, language)) {
            context.toastFromMainThread(R.string.select_language)
            tess.recycle()
            return null
        }

        val bitmap = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        } ?: return null

        tess.setImage(bitmap)

        return tess.utF8Text.also {
            tess.recycle()
        }
    }

    fun downloadLanguageData(context: Context, languagePath: String) {
        val url = "$baseUrl/$languagePath"
        val targetFile = File(getTessDir(context), languagePath)

        val downloadService = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
            .setDestinationUri(Uri.fromFile(targetFile))
            .setTitle("Downloading $languagePath ...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        downloadService.enqueue(request)
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
