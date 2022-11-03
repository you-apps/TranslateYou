package com.bnyro.translate.util

import com.bnyro.translate.obj.Language
import com.bnyro.translate.obj.Translation
import java.net.URL

abstract class TranslationEngine(
    val name: String,
    val defaultUrl: String,
    val urlModifiable: Boolean,
    val apiKeyState: Int,
    val autoLanguageCode: String,
    var supportsSimTranslation: Boolean = true
) {

    abstract fun create(): TranslationEngine

    abstract suspend fun getLanguages(): List<Language>

    abstract suspend fun translate(query: String, source: String, target: String): Translation

    val urlPrefKey = this.name + Preferences.instanceUrlKey
    val apiPrefKey = this.name + Preferences.apiKey
    val simPrefKey = this.name + Preferences.simultaneousTranslationKey

    fun getUrl(): String {
        val url = Preferences.get(
            urlPrefKey,
            this.defaultUrl
        )

        return try {
            URL(url)
            url
        } catch (e: Exception) {
            defaultUrl
        }
    }

    fun getApiKey() = Preferences.get(
        apiPrefKey,
        ""
    )

    fun sourceOrAuto(source: String): String {
        return if (source == "") autoLanguageCode else source
    }

    fun isSimultaneousTranslationEnabled() = Preferences.get(
        simPrefKey,
        false
    )
}
