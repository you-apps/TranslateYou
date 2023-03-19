package com.bnyro.translate.api.st

import android.accounts.NetworkErrorException
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class STEngine : TranslationEngine(
    name = "SimplyTranslate",
    defaultUrl = "https://simplytranslate.org/",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = "auto"
) {
    lateinit var api: SimplyTranslate
    val selEnginePrefKey = this.name + "selectedEngine"

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val body = api.getLanguages(getSelectedEngine())

        // val body = api.getLanguages().execute().body()
        val languages = mutableListOf<Language>()
        body.split("\n").let {
            for (index in 0..(it.size.toDouble() / 2 - 1).toInt()) {
                languages.add(
                    Language(name = it[index * 2], code = it[index * 2 + 1])
                )
            }
        }

        if (languages.isEmpty()) throw NetworkErrorException("Network Error")
        return languages
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            engine = getSelectedEngine(),
            source = sourceOrAuto(source),
            query = query,
            target = target
        )
        return Translation(
            translatedText = response.translated_text,
            detectedLanguage = response.source_language
        )
    }

    private fun getSelectedEngine(): String? {
        Preferences.get(selEnginePrefKey, "all").let {
            return when (it) {
                "all" -> null
                else -> it
            }
        }
    }
}
