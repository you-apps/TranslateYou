package com.bnyro.translate.constants

import com.bnyro.translate.BuildConfig
import com.bnyro.translate.api.lt.LTHelper
import com.bnyro.translate.api.lt.LibreTranslate
import com.bnyro.translate.api.lv.LVHelper
import com.bnyro.translate.api.lv.LingvaTranslate
import com.bnyro.translate.obj.TranslationEngine
import com.bnyro.translate.util.RetrofitHelper

object TranslationEngines {
    var engines = createEngines()

    private fun createEngines(): List<TranslationEngine> {
        val engines = mutableListOf<TranslationEngine>()

        val libreTranslate = TranslationEngine(
            id = 0,
            name = "LibreTranslate",
            defaultUrl = "https://libretranslate.de",
            apiHelper = LTHelper(
                RetrofitHelper.createApi("https://libretranslate.de", LibreTranslate::class.java)
            ),
            urlModifiable = true,
            apiKeyState = ApiKeyState.OPTIONAL
        )

        engines.add(libreTranslate)

        if (BuildConfig.FLAVOR == "libre") return engines

        val lingvaTranslate = TranslationEngine(
            id = 1,
            name = "LingvaTranslate",
            defaultUrl = "https://lingva.ml",
            apiHelper = LVHelper(
                RetrofitHelper.createApi("https://lingva.ml", LingvaTranslate::class.java)
            ),
            urlModifiable = true,
            apiKeyState = ApiKeyState.DISABLED
        )

        engines.add(lingvaTranslate)

        return engines
    }

    fun update() {
        engines = createEngines()
    }
}
