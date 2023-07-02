/*
 * Copyright (c) 2023 Bnyro
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
import com.bnyro.translate.R
import com.bnyro.translate.ext.toastFromMainThread
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File

object TessHelper {
    val availableLanguages = listOf(
        "tgk",
        "hrv",
        "spa_old",
        "jpn_vert",
        "khm",
        "mri",
        "uzb",
        "bel",
        "swa",
        "chi_sim",
        "chr",
        "ukr",
        "srp",
        "kat_old",
        "ceb",
        "nep",
        "uzb_cyrl",
        "vie",
        "rus",
        "gle",
        "sqi",
        "uig",
        "pol",
        "lat",
        "lav",
        "jpn",
        "ind",
        "srp_latn",
        "epo",
        "san",
        "kir",
        "ces",
        "chi_tra",
        "nor",
        "mal",
        "nld",
        "slk",
        "hin",
        "tur",
        "fao",
        "fin",
        "bul",
        "ita",
        "yid",
        "asm",
        "tgl",
        "frk",
        "hun",
        "kmr",
        "pan",
        "deu_frak",
        "osd",
        "ron",
        "cos",
        "aze_cyrl",
        "mar",
        "lao",
        "fas",
        "isl",
        "enm",
        "eus",
        "jav",
        "spa",
        "cat",
        "ltz",
        "tat",
        "por",
        "est",
        "afr",
        "tha",
        "dzo",
        "lit",
        "que",
        "hat",
        "deu",
        "syr",
        "kat",
        "kan",
        "fry",
        "fil",
        "tir",
        "frm",
        "gla",
        "mya",
        "ell",
        "slk_frak",
        "chi_tra_vert",
        "oci",
        "ori",
        "slv",
        "chi_sim_vert",
        "pus",
        "kor_vert",
        "iku",
        "eng",
        "cym",
        "guj",
        "swe",
        "glg",
        "fra",
        "mlt",
        "snd",
        "ben",
        "yor",
        "mon",
        "tam",
        "bod",
        "bre",
        "msa",
        "ara",
        "ita_old",
        "sun",
        "dan_frak",
        "sin",
        "amh",
        "equ",
        "heb",
        "kaz",
        "ton",
        "kor",
        "tel",
        "bos",
        "urd",
        "aze",
        "mkd",
        "div",
        "dan",
        "hye",
        "grc"
    ).sorted()

    private const val baseUrl = "https://raw.githubusercontent.com/tesseract-ocr/tessdata/main"

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

    fun downloadLanguageData(context: Context, language: String) {
        val url = "$baseUrl/$language.traineddata"
        val targetFile = File(getTessDir(context), "$language.traineddata")

        val downloadService = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
            .setDestinationUri(Uri.fromFile(targetFile))
            .setTitle("Downloading $language ...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        downloadService.enqueue(request)
    }

    fun getAvailableLanguages(context: Context): List<String> {
        return getTessDir(context).listFiles().orEmpty().map {
            it.nameWithoutExtension
        }
    }

    fun deleteLanguage(context: Context, language: String): Boolean {
        val file = File(getTessDir(context), "$language.traineddata")
        return if (file.exists()) file.delete() else false
    }

    fun areLanguagesAvailable(context: Context): Boolean {
        return getTessDir(context).listFiles()?.isNotEmpty() ?: false
    }

    private fun getRootDir(context: Context) = context.getExternalFilesDir(null)!!

    private fun getTessDir(context: Context) = File(getRootDir(context), "tessdata").also {
        if (!it.exists()) it.mkdirs()
    }
}
