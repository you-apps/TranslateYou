package com.bnyro.translate.api.mm

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import com.fasterxml.jackson.databind.ObjectMapper

class MMEngine : TranslationEngine(
    name = "MyMemory",
    defaultUrl = "https://api.mymemory.translated.net",
    urlModifiable = false,
    apiKeyState = ApiKeyState.OPTIONAL,
    autoLanguageCode = "Autodetect"
) {
    lateinit var api: MyMemory
    override fun create(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val mapper = ObjectMapper()
        val languages = mutableListOf<Language>()

        val json = try {
            api.getLanguages()
        } catch (e: Exception) {
            return listOf()
        }
        mapper.readTree(
            mapper.writeValueAsString(json)
        ).fields().forEach {
            val code = it.value.get("c").textValue()
            languages.add(
                Language(
                    name = it.key,
                    code = code
                )
            )
        }

        return languages
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val key = getApiKey()
        val response = api.translate(
            query,
            "${sourceOrAuto(source)}|$target",
            if (key == "") null else key

        )
        return Translation(
            translatedText = response.responseData?.translatedText ?: "",
            detectedLanguage = response.responseData?.detectedLanguage
        )
    }
}
