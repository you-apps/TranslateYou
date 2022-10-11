package com.bnyro.translate.constants

import com.bnyro.translate.BuildConfig
import com.bnyro.translate.api.lt.LTHelper
import com.bnyro.translate.api.lv.LVHelper
import com.bnyro.translate.obj.TranslationEngine

val libreTranslateEngine = TranslationEngine(
    id = 0,
    name = "LibreTranslate",
    defaultUrl = "https://libretranslate.de",
    apiHelper = LTHelper(),
    urlModifiable = true,
    apiKeyState = ApiKeyState.OPTIONAL
)

val lingvaTranslateEngine = TranslationEngine(
    id = 1,
    name = "LingvaTranslate",
    defaultUrl = "https://lingva.ml",
    apiHelper = LVHelper(),
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED
)

val supportedEngines = mutableListOf(
    libreTranslateEngine
).apply {
    if (BuildConfig.FLAVOR != "libre") {
        this.add(lingvaTranslateEngine)
    }
}
