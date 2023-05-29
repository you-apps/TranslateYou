package com.bnyro.translate.const

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
        GlEngine()
    ).map {
        it.createOrRecreate()
    }

    fun updateAll() = engines.forEach { it.createOrRecreate() }
}
