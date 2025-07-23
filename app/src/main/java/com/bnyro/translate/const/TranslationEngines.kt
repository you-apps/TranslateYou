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

package com.bnyro.translate.const

import com.bnyro.translate.api.ap.ApEngine
import com.bnyro.translate.api.deepl.DeeplAuthenticatedEngine
import com.bnyro.translate.api.deepl.DeeplBrowserEngine
import com.bnyro.translate.api.gl.GlEngine
import com.bnyro.translate.api.kagi.KagiEngine
import com.bnyro.translate.api.la.LaEngine
import com.bnyro.translate.api.lt.LTEngine
import com.bnyro.translate.api.lv.LVEngine
import com.bnyro.translate.api.mh.MhEngine
import com.bnyro.translate.api.mm.MMEngine
import com.bnyro.translate.api.or.OneRingEngine
import com.bnyro.translate.api.po.PonsEngine
import com.bnyro.translate.api.st.STEngine
import com.bnyro.translate.api.wm.WmEngine
import com.bnyro.translate.api.ya.YandexEngine

object TranslationEngines {
    val engines = listOf(
        LTEngine(),
        LVEngine(),
        DeeplAuthenticatedEngine(),
        DeeplBrowserEngine(),
        KagiEngine(),
        MMEngine(),
        YandexEngine(),
        STEngine(),
        MhEngine(),
        WmEngine(),
        GlEngine(),
        ApEngine(),
        OneRingEngine(),
        PonsEngine(),
        LaEngine()
    ).map {
        it.createOrRecreate()
    }

    fun updateAll() = engines.forEach { it.createOrRecreate() }
}
