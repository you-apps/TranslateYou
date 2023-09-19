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
import com.bnyro.translate.api.deepl.DeeplEngine
import com.bnyro.translate.api.gl.GlEngine
import com.bnyro.translate.api.lt.LTEngine
import com.bnyro.translate.api.lv.LVEngine
import com.bnyro.translate.api.mm.MMEngine
import com.bnyro.translate.api.reverso.ReversoEngine
import com.bnyro.translate.api.st.STEngine
import com.bnyro.translate.api.wm.WmEngine

object TranslationEngines {
    val engines = listOf(
        LTEngine(),
        LVEngine(),
        DeeplEngine(),
        MMEngine(),
        ReversoEngine(),
        STEngine(),
        WmEngine(),
        GlEngine(),
        ApEngine()
    ).map {
        it.createOrRecreate()
    }

    fun updateAll() = engines.forEach { it.createOrRecreate() }
}
