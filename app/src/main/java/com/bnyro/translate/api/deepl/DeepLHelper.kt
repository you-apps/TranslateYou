package com.bnyro.translate.api.deepl

import com.bnyro.translate.api.APIHelper
import com.bnyro.translate.constants.TranslationEngines
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.Preferences

class DeepLHelper(
    private val api: DeepL
) : APIHelper() {
    val apiKeyString = "DeepL-Auth-Key " + Preferences.getApiKeyByEngine(
        TranslationEngines.engines.filter { it.apiHelper is DeepLHelper }.first()
    )

    override suspend fun getLanguages(): List<Language> = api.getLanguages(
        apiKeyString
    ).map {
        Language(
            code = it.language,
            name = it.name
        )
    }.toMutableList().apply {
        add(
            0,
            Language(
                "",
                "Auto"
            )
        )
    }

    override suspend fun translate(query: String, source: String, target: String): String =
        api.translate(
            apiKeyString,
            source,
            target,
            query
        ).translations[0].text
}
