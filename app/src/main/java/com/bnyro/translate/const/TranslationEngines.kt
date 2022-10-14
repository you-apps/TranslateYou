package com.bnyro.translate.const

import com.bnyro.translate.BuildConfig
import com.bnyro.translate.api.deepl.DeeplEngine
import com.bnyro.translate.api.lt.LTEngine
import com.bnyro.translate.api.lv.LVEngine
import com.bnyro.translate.api.mm.MMEngine
import com.bnyro.translate.util.TranslationEngine

object TranslationEngines {
    val libreTranslate = LTEngine()
    var lingvaTranslate = LVEngine()
    var deepl = DeeplEngine()
    var myMemory = MMEngine()

    var engines = createEngines()

    private fun createEngines(): List<TranslationEngine> {
        val engines = mutableListOf<TranslationEngine>()

        engines.add(libreTranslate.create())

        if (BuildConfig.FLAVOR == "libre") return engines

        listOf(lingvaTranslate, deepl, myMemory).forEach {
            engines.add(it.create())
        }

        return engines
    }

    fun update() {
        engines.forEach {
            it.create()
        }
    }
}
