package com.bnyro.translate.constants

import com.bnyro.translate.BuildConfig
import com.bnyro.translate.api.lt.LTHelper
import com.bnyro.translate.api.lt.LibreTranslate
import com.bnyro.translate.api.lv.LVHelper
import com.bnyro.translate.api.lv.LingvaTranslate
import com.bnyro.translate.util.TranslationEngine

object TranslationEngines {
    var engines = createEngines()

    private fun createEngines(): List<TranslationEngine> {
        val engines = mutableListOf<TranslationEngine>()

        val libreTranslate = TranslationEngine(
            id = 0,
            name = "LibreTranslate",
            defaultUrl = "https://libretranslate.de",
            urlModifiable = true,
            apiKeyState = ApiKeyState.OPTIONAL
        )

        libreTranslate.apiHelper = LTHelper(libreTranslate.createApi(LibreTranslate::class.java))

        engines.add(libreTranslate)

        if (BuildConfig.FLAVOR == "libre") return engines

        val lingvaTranslate = TranslationEngine(
            id = 1,
            name = "LingvaTranslate",
            defaultUrl = "https://lingva.ml",
            urlModifiable = true,
            apiKeyState = ApiKeyState.DISABLED
        )

        lingvaTranslate.apiHelper = LVHelper(lingvaTranslate.createApi(LingvaTranslate::class.java))

        engines.add(lingvaTranslate)

        return engines
    }

    fun update() {
        engines = createEngines()
    }
}
