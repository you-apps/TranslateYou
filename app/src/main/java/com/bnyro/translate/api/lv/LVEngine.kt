package com.bnyro.translate.api.lv

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.ext.expOrNull
import com.bnyro.translate.obj.Definition
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class LVEngine : TranslationEngine(
    name = "Lingva",
    defaultUrl = "https://lingva.ml",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = "auto"
) {

    private lateinit var api: LingvaTranslate
    override fun create(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().languages.toMutableList().apply {
            removeAt(0)
        }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            sourceOrAuto(source),
            target,
            query.replace("/", "")
        )
        return Translation(
            translatedText = response.translation,
            detectedLanguage = response.info?.detectedSource,
            transliterations = listOfNotNull(response.info?.pronunciation?.query),
            examples = response.info?.examples,
            similar = response.info?.similar,
            definitions = response.info?.definitions
                ?.map {
                    Definition(
                        type = it.type,
                        definition = expOrNull { it.list.first().definition },
                        example = expOrNull { it.list.first().example },
                        synonym = expOrNull { it.list.first().synonyms.first() }
                    )
                }
        )
    }
}
