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

package com.bnyro.translate

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.bnyro.translate.ext.toastFromMainThread
import com.bnyro.translate.util.EnginePreferencesProviderImpl
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.SpeechHelper
import net.youapps.translation_engines.TranslationEngine
import net.youapps.translation_engines.TranslationEngines

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Preferences.initialize(
            this
        )

        DatabaseHolder().initDb(
            this
        )

        SpeechHelper.initTTS(this)

        // initialize all translation engines
        updateAllTranslationEngines()
    }

    private fun updateAllTranslationEngines() {
        for (engine in translationEngines) {
            try {
                engine.createOrRecreate()
            } catch (e: Exception) {
                toastFromMainThread(
                    getString(
                        R.string.failed_to_initialize_engine,
                        engine.name,
                        e.message.orEmpty()
                    ),
                    length = Toast.LENGTH_LONG
                )
            }
        }
    }

    companion object {
        val translationEngines: List<TranslationEngine> = TranslationEngines.getAllEngines(
            EnginePreferencesProviderImpl()
        )
    }
}
