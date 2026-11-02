/*
 * Copyright (c) 2026 You Apps
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

package net.youapps.translation_engines.iciba

import java.security.MessageDigest
import java.util.*
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine
import net.youapps.translation_engines.iciba.obj.IcibaRequest

class IcibaEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name: String = "Iciba"
    override val defaultUrl: String = "https://dictionary.iciba.com"
    override val urlModifiable: Boolean = false
    override val apiKeyState: ApiKeyState = ApiKeyState.DISABLED

    private lateinit var api: Iciba

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val languageCodes = api.getLanguages().values.flatMap { it.keys }
            .filter { it.isNotBlank() }

        return languageCodes.mapNotNull { code ->
            val languageName = Locale(code).displayLanguage
            languageName.takeIf { it != code }?.let { Language(code = code, name = it) }
        }.distinct()
    }

    override suspend fun translate(
        query: String,
        source: String?,
        target: String
    ): Translation {
        val now = System.currentTimeMillis().toString()

        val md5 = MessageDigest.getInstance("MD5")
        val hashInput =
            "/dictionary/fy/batch" + "6" + "1000006" + now + "7ece94d9f9c202b0d2ec557dg4r9bc"
        md5.update(hashInput.toByteArray())
        val signature = md5.digest().joinToString("") { it.toHexString() }

        val translationResponse = api.translate(
            timestamp = now,
            signature = signature.lowercase(),
            request = IcibaRequest(
                from = source ?: "auto",
                to = target,
                textList = listOf(query)
            )
        ).data.first()

        return Translation(
            translatedText = translationResponse.out,
            detectedLanguage = translationResponse.from
        )
    }
}