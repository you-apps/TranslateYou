package com.bnyro.translate.util

import com.bnyro.translate.obj.Language
import java.net.URL

abstract class TranslationEngine(
    val name: String,
    val defaultUrl: String,
    val urlModifiable: Boolean,
    val apiKeyState: Int
) {

    abstract fun create(): TranslationEngine

    abstract suspend fun getLanguages(): List<Language>

    abstract suspend fun translate(query: String, source: String, target: String): String

    val urlPrefKey = this.name + Preferences.instanceUrlKey
    val apiPrefKey = this.name + Preferences.apiKey
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
}
