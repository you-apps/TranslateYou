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
import com.bnyro.translate.db.obj.Language
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
        Language("en", "English"),
        Language("ar", "Arabic"),
        Language("az", "Azerbaijani"),
        Language("be", "Belarusian"),
        Language("bg", "Bulgarian"),
        Language("bn", "Bengali"),
        Language("ca", "Catalan"),
        Language("cs", "Czech"),
        Language("da", "Danish"),
        Language("de", "German"),
        Language("es", "Spanish"),
        Language("et", "Estonian"),
        Language("fa", "Persian"),
        Language("fi", "Finnish"),
        Language("fil", "Filipino"),
        Language("fr-rFR", "French"),
        Language("he", "Hebrew"),
        Language("hi", "Hindi"),
        Language("hu", "Hungarian"),
        Language("ia", "Interlingua"),
        Language("id", "Indonesian"),
        Language("it", "Italian"),
        Language("ja", "Japanese"),
        Language("kab", "Kabyle"),
        Language("ko", "Korean"),
        Language("lt", "Lithuanian"),
        Language("ml", "Malayalam"),
        Language("ms", "Malay"),
        Language("nb-rNO", "Norwegian Bokmål"),
        Language("nl", "Dutch"),
        Language("nn", "Norwegian Nynorsk"),
        Language("or", "Odia"),
        Language("pa", "Punjabi"),
        Language("pa-rPK", "Punjabi (Pakistan)"),        
        Language("pl", "Polish"),
        Language("pt", "Portuguese"),
        Language("pt-rBR", "Portuguese (Brazil)"),
        Language("ro", "Romanian"),
        Language("ru", "Russian"),
        Language("sat", "Santali"),
        Language("sc", "Sardinian"),
        Language("sr", "Serbian"),
        Language("sv", "Swedish"),
        Language("ta", "Tamil"),
        Language("tr", "Turkish"),
        Language("tt", "Tatar"),
        Language("uk", "Ukrainian"),
        Language("vi", "Vietnamese"),
        Language("zh-rCN", "Chinese (Simplified)"),
        Language("zh-rTW", "Chinese (Traditional)")
    ).sortedBy { it.name }.toMutableList()
        .apply {
            add(0, Language("", context.getString(R.string.system)))
        }
}
