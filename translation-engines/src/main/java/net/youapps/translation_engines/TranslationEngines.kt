/*
 * Copyright (c) 2026 You Apps
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

package net.youapps.translation_engines

import net.youapps.translation_engines.ap.ApEngine
import net.youapps.translation_engines.deepl.DeeplAuthenticatedFreeApiEngine
import net.youapps.translation_engines.deepl.DeeplAuthenticatedPaidApiEngine
import net.youapps.translation_engines.deepl.DeeplBrowserEngine
import net.youapps.translation_engines.gl.GlEngine
import net.youapps.translation_engines.kagi.KagiEngine
import net.youapps.translation_engines.la.LaEngine
import net.youapps.translation_engines.lt.LTEngine
import net.youapps.translation_engines.lv.LVEngine
import net.youapps.translation_engines.mh.MhEngine
import net.youapps.translation_engines.mm.MMEngine
import net.youapps.translation_engines.or.OneRingEngine
import net.youapps.translation_engines.po.PonsEngine
import net.youapps.translation_engines.st.STEngine
import net.youapps.translation_engines.wm.WmEngine
import net.youapps.translation_engines.ya.YandexEngine

object TranslationEngines {
    fun getAllEngines(settingsProvider: EngineSettingsProvider) = listOf(
        LTEngine(settingsProvider),
        LVEngine(settingsProvider),
        DeeplAuthenticatedFreeApiEngine(settingsProvider),
        DeeplAuthenticatedPaidApiEngine(settingsProvider),
        DeeplBrowserEngine(settingsProvider),
        KagiEngine(settingsProvider),
        MMEngine(settingsProvider),
        YandexEngine(settingsProvider),
        STEngine(settingsProvider),
        MhEngine(settingsProvider),
        WmEngine(settingsProvider),
        GlEngine(settingsProvider),
        ApEngine(settingsProvider),
        OneRingEngine(settingsProvider),
        PonsEngine(settingsProvider),
        LaEngine(settingsProvider)
    )
}