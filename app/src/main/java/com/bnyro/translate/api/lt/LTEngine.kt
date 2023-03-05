package com.bnyro.translate.api.lt

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class LTEngine : TranslationEngine(
    name = "LibreTranslate",
    defaultUrl = "https://translate.argosopentech.com",
    urlModifiable = true,
    apiKeyState = ApiKeyState.OPTIONAL,
    autoLanguageCode = "auto"
) {

    private lateinit var api: LibreTranslate
    override fun create(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().map {
            Language(it.code!!, it.name!!)
        }
    }

    override suspend fun translate(
        query: String,
        source: String,
        target: String
    ): Translation {
        val response = api.translate(
            query,
            sourceOrAuto(source),
            target,
            getApiKey()
        )
        return Translation(
            translatedText = response.translatedText,
            detectedLanguage = response.detectedLanguage?.language
        )
    }
}
