package com.bnyro.translate.constants

import com.bnyro.translate.BuildConfig
import com.bnyro.translate.api.deepl.DeepL
import com.bnyro.translate.api.deepl.DeepLHelper
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

        var deeplEngine = TranslationEngine(
            id = 2,
            name = "DeepL",
            defaultUrl = "https://api-free.deepl.com",
            urlModifiable = false,
            apiKeyState = ApiKeyState.REQUIRED
        )

        lingvaTranslate.apiHelper = LVHelper(lingvaTranslate.createApi(LingvaTranslate::class.java))
        deeplEngine.apiHelper = DeepLHelper(deeplEngine.createApi(DeepL::class.java))

        listOf(lingvaTranslate, deeplEngine).forEach {
            engines.add(it)
        }

        return engines
    }

    fun update() {
        engines = createEngines()
    }
}
