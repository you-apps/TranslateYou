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
import android.content.res.Configuration
import com.bnyro.translate.R
import com.bnyro.translate.db.obj.DbLanguage
import java.util.*

object LocaleHelper {
    fun updateLanguage(context: Context) {
        val langPref = Preferences.get(Preferences.appLanguageKey, "")
        val locale = when {
            langPref.isEmpty() -> Locale.getDefault()
            langPref.contains("-") -> Locale(
                langPref.substringBefore("-"),
                langPref.substringAfter("r")
            )
            else -> Locale(langPref)
        }
        updateResources(context, locale)
    }

    private fun updateResources(context: Context, locale: Locale) {
        context.resources.apply {
            val config = Configuration(configuration)

            context.createConfigurationContext(configuration)
            Locale.setDefault(locale)
            config.setLocale(locale)

            @Suppress("DEPRECATION")
            updateConfiguration(config, displayMetrics)
        }
    }

    fun getLanguages(context: Context) = listOf(
        DbLanguage("en", "English"),
        DbLanguage("ar", "Arabic"),
        DbLanguage("az", "Azerbaijani"),
        DbLanguage("be", "Belarusian"),
        DbLanguage("bg", "Bulgarian"),
        DbLanguage("bn", "Bengali"),
        DbLanguage("ca", "Catalan"),
        DbLanguage("cs", "Czech"),
        DbLanguage("da", "Danish"),
        DbLanguage("de", "German"),
        DbLanguage("es", "Spanish"),
        DbLanguage("et", "Estonian"),
        DbLanguage("fa", "Persian"),
        DbLanguage("fi", "Finnish"),
        DbLanguage("fil", "Filipino"),
        DbLanguage("fr-rFR", "French"),
        DbLanguage("he", "Hebrew"),
        DbLanguage("hi", "Hindi"),
        DbLanguage("hu", "Hungarian"),
        DbLanguage("ia", "Interlingua"),
        DbLanguage("id", "Indonesian"),
        DbLanguage("it", "Italian"),
        DbLanguage("ja", "Japanese"),
        DbLanguage("kab", "Kabyle"),
        DbLanguage("ko", "Korean"),
        DbLanguage("lt", "Lithuanian"),
        DbLanguage("ml", "Malayalam"),
        DbLanguage("ms", "Malay"),
        DbLanguage("nb-rNO", "Norwegian Bokmål"),
        DbLanguage("nl", "Dutch"),
        DbLanguage("nn", "Norwegian Nynorsk"),
        DbLanguage("or", "Odia"),
        DbLanguage("pa", "Punjabi"),
        DbLanguage("pa-rPK", "Punjabi (Pakistan)"),
        DbLanguage("pl", "Polish"),
        DbLanguage("pt", "Portuguese"),
        DbLanguage("pt-rBR", "Portuguese (Brazil)"),
        DbLanguage("ro", "Romanian"),
        DbLanguage("ru", "Russian"),
        DbLanguage("sat", "Santali"),
        DbLanguage("sc", "Sardinian"),
        DbLanguage("sr", "Serbian"),
        DbLanguage("sv", "Swedish"),
        DbLanguage("ta", "Tamil"),
        DbLanguage("tr", "Turkish"),
        DbLanguage("tt", "Tatar"),
        DbLanguage("uk", "Ukrainian"),
        DbLanguage("vi", "Vietnamese"),
        DbLanguage("zh-rCN", "Chinese (Simplified)"),
        DbLanguage("zh-rTW", "Chinese (Traditional)")
    ).sortedBy { it.name }.toMutableList()
        .apply {
            add(0, DbLanguage("", context.getString(R.string.system)))
        }
}
