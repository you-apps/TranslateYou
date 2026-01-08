/*
 * Copyright (c) 2023 You Apps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bnyro.translate.util

import android.content.Context
import android.content.SharedPreferences
import com.bnyro.translate.const.ThemeMode
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.TranslationEngine
import androidx.core.content.edit

object Preferences {
    const val selectedEngineKey = "selectedEngine"
    const val historyEnabledKey = "historyEnabledKey"
    const val skipSimilarHistoryKey = "skipSimilarHistory"
    const val translateAutomatically = "translateAutomatically"
    const val fetchDelay = "fetchDelay"
    const val compactHistory = "compactHistory"
    const val simultaneousTranslationKey = "simultaneousTranslation"
    const val showAdditionalInfo = "showAdditionalInfoKey"
    const val appLanguageKey = "appLanguage"
    const val charCounterLimitKey = "charCountLimit"
    const val tessLanguageKey = "tessLanguage"

    const val themeModeKey = "themeModeKey"
    const val accentColorKey = "accentColor"
    const val sourceLanguage = "sourceLanguage"
    const val targetLanguage = "targetLanguage"

    // Engine specific
    private const val instanceUrlKey = "instanceUrl"
    private const val apiKey = "apiKey"
    private const val selectedModel = "selectedEngine"

    lateinit var prefs: SharedPreferences

    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(
            "preferences",
            Context.MODE_PRIVATE
        )
    }

    fun <T> put(key: String, value: T) {
        when (value) {
            is Boolean -> prefs.edit { putBoolean(key, value) }
            is String -> prefs.edit { putString(key, value) }
            is Int -> prefs.edit { putInt(key, value) }
            is Float -> prefs.edit { putFloat(key, value) }
            is Long -> prefs.edit { putLong(key, value) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, defValue: T): T {
        return when (defValue) {
            is Boolean -> prefs.getBoolean(key, defValue) as T
            is Int -> (prefs.getInt(key, defValue)) as T
            is Long -> (prefs.getLong(key, defValue)) as T
            is Float -> (prefs.getFloat(key, defValue)) as T
            else -> (prefs.getString(key, defValue.toString()) ?: defValue) as T
        }
    }

    fun getThemeMode() =
        ThemeMode.entries[get(themeModeKey, ThemeMode.AUTO.value.toString()).toInt()]

    fun getAccentColor() = prefs.getString(accentColorKey, null)

    // Engine specific settings
    fun apiKeyPrefKey(engine: TranslationEngine): String = engine.name + apiKey
    fun apiUrlPrefKey(engine: TranslationEngine): String = engine.name + instanceUrlKey
    fun selectedModelPrefKey(engine: TranslationEngine): String = engine.name + selectedModel
    fun simTranslationPrefKey(engine: TranslationEngine): String = engine.name + simultaneousTranslationKey

    fun isSimultaneousTranslationEnabled(engine: TranslationEngine): Boolean {
        val simTranslationKey = engine.name + simultaneousTranslationKey

        return get(simTranslationKey, false)
    }
}

class EnginePreferencesProviderImpl : EngineSettingsProvider {
    override fun getApiUrl(engine: TranslationEngine): String? {
        val urlPrefKey = Preferences.apiUrlPrefKey(engine)

        return Preferences.get(urlPrefKey, "").ifEmpty { null }
    }

    override fun getApiKey(engine: TranslationEngine): String? {
        val apiKeyPrefKey = Preferences.apiKeyPrefKey(engine)

        return Preferences.get(apiKeyPrefKey, "").ifEmpty { null }
    }

    override fun getSelectedModel(engine: TranslationEngine): String? {
        val selectedModelPrefKey = Preferences.selectedModelPrefKey(engine)

        return Preferences.get(selectedModelPrefKey, "").ifEmpty { null }
    }

}