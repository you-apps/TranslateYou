package com.bnyro.translate.api.lv

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.util.TranslationEngine

class LVEngine : TranslationEngine(
    name = "Lingva",
    defaultUrl = "https://lingva.ml",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED
) {
    override fun create(): TranslationEngine = apply {
        apiHelper = LVHelper()
    }
}
