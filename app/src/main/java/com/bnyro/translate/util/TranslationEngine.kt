package com.bnyro.translate.util

import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

abstract class TranslationEngine(
    val name: String,
    val defaultUrl: String,
    val urlModifiable: Boolean,
    val apiKeyState: Int,
    val autoLanguageCode: String,
    var supportsSimTranslation: Boolean = true
) {

    abstract fun createOrRecreate(): TranslationEngine

    abstract suspend fun getLanguages(): List<Language>

    abstract suspend fun translate(query: String, source: String, target: String): Translation

    val urlPrefKey = this.name + Preferences.instanceUrlKey
    val apiPrefKey = this.name + Preferences.apiKey
    val simPrefKey = this.name + Preferences.simultaneousTranslationKey

    open fun getUrl(): String {
        return Preferences.get(
            urlPrefKey,
            this.defaultUrl
        ).toHttpUrlOrNull()?.toString() ?: defaultUrl
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
