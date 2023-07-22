/*
 * Copyright (c) 2023 Bnyro
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

object Preferences {
    const val instanceUrlKey = "instanceUrl"
    const val apiKey = "apiKey"
    const val apiTypeKey = "apiTypeKey"
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

    lateinit var prefs: SharedPreferences

    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(
            "preferences",
            Context.MODE_PRIVATE
        )
    }

    fun <T> put(key: String, value: T) {
        when (value) {
            is Boolean -> prefs.edit().putBoolean(key, value).apply()
            is String -> prefs.edit().putString(key, value).apply()
            is Int -> prefs.edit().putInt(key, value).apply()
            is Float -> prefs.edit().putFloat(key, value).apply()
            is Long -> prefs.edit().putLong(key, value).apply()
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

    fun getThemeMode() = ThemeMode.values()[get(themeModeKey, ThemeMode.AUTO.value.toString()).toInt()]

    fun getAccentColor() = prefs.getString(accentColorKey, null)
}
