package com.bnyro.translate.api.mm

import android.util.Log
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.JsonHelper
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.jsonObject

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
        val json = try {
            api.getLanguages()
        } catch (e: Exception) {
            Log.e("fetching", e.toString())
            return listOf()
        }
        val el = JsonHelper.json.parseToJsonElement(
            JsonHelper.json.encodeToString(json)
        )

        Log.e("json", JsonHelper.json.encodeToString(json))

        return el.jsonObject.entries.map {
            val code = it.value.jsonObject["c"].toString()
            Log.e("code", code)
            Language(
                name = it.key,
                code = code
            )
        }
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
