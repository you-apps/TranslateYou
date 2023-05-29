package com.bnyro.translate.api.wm

import com.bnyro.translate.api.wm.obj.WmTranslationRequest
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.util.*

class WmEngine: TranslationEngine(
    name = "MinT",
    apiKeyState = ApiKeyState.DISABLED,
    supportsSimTranslation = true,
    autoLanguageCode = null,
    defaultUrl = "https://translate.wmcloud.org/",
    urlModifiable = true
) {
    lateinit var api: WikimediaMinT

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val json = api.getLanguages()
        return json.entries.map {
            val languageName = runCatching { Locale.forLanguageTag(it.key).displayName }.getOrNull()
            Language(it.key, languageName ?: it.key)
        }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(source, target, WmTranslationRequest(query))
        return Translation(response.translation)
    }
}