package com.bnyro.translate.api.mm

import com.bnyro.translate.api.APIHelper
import com.bnyro.translate.const.TranslationEngines
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.RetrofitHelper
import com.fasterxml.jackson.databind.ObjectMapper

class MMHelper : APIHelper() {
    val api = RetrofitHelper.createApi(
        TranslationEngines.myMemory,
        MyMemory::class.java
    )

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

    override suspend fun translate(query: String, source: String, target: String): String {
        val key = TranslationEngines.myMemory.getApiKey()
        return api.translate(
            query,
            "$source|$target",
            if (key == "") null else key

        )
            .matches
            .first()
            .translation
    }
}
