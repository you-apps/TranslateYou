package com.bnyro.translate.api.reverso

import com.bnyro.translate.api.reverso.obj.ReversoRequestBody
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.ext.concatenate
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class ReversoEngine : TranslationEngine(
    name = "Reverso",
    defaultUrl = "https://api.reverso.net/",
    urlModifiable = false,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = "auto",
    supportsSimTranslation = false
) {
    lateinit var api: Reverso

    override fun create(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return listOf(
            Language("ara", "Arabic"),
            Language("chi", "Chinese (Simplified)"),
            Language("dut", "Dutch"),
            Language("eng", "English"),
            Language("fra", "French"),
            Language("ger", "German"),
            Language("heb", "Hebrew"),
            Language("ita", "Italian"),
            Language("jpn", "Japanese"),
            Language("kor", "Korean"),
            Language("pol", "Polish"),
            Language("por", "Portuguese"),
            Language("rum", "Romanian"),
            Language("rus", "Russian"),
            Language("spa", "Spanish"),
            Language("swe", "Swedish"),
            Language("tur", "Turkish"),
            Language("ukr", "Ukrainian")
        )
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            requestBody = ReversoRequestBody(
                from = sourceOrAuto(source),
                to = target,
                input = query
            )
        )

        return Translation(
            translatedText = response.translation.firstOrNull() ?: "",
            detectedLanguage = response.languageDetection?.detectedLanguage,
            similar = response.contextResults?.results
                ?.filter { it.translation != null }
                ?.map { it.translation!! },
            examples = response.contextResults?.results?.let { result ->
                concatenate(
                    result.map { it.sourceExamples }.flatten(),
                    result.map { it.targetExamples }.flatten()
                )
            }
        )
    }
}
