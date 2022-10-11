package com.bnyro.translate.constants

import com.bnyro.translate.BuildConfig
import com.bnyro.translate.api.deepl.DeepLHelper
import com.bnyro.translate.api.deepl.DeeplEngine
import com.bnyro.translate.api.lt.LTEngine
import com.bnyro.translate.api.lt.LTHelper
import com.bnyro.translate.api.lv.LVEngine
import com.bnyro.translate.api.lv.LVHelper
import com.bnyro.translate.util.TranslationEngine

object TranslationEngines {
    val libreTranslate = LTEngine()
    var lingvaTranslate = LVEngine()
    var deepl = DeeplEngine()

    var engines = createEngines()

    private fun createEngines(): List<TranslationEngine> {
        val engines = mutableListOf<TranslationEngine>()

        libreTranslate.apiHelper = LTHelper()

        engines.add(libreTranslate)

        if (BuildConfig.FLAVOR == "libre") return engines

        lingvaTranslate.apiHelper = LVHelper()
        deepl.apiHelper = DeepLHelper()

        listOf(lingvaTranslate, deepl).forEach {
            engines.add(it)
        }

        return engines
    }

    fun update() {
        engines = createEngines()
    }
}
