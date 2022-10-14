package com.bnyro.translate.api.mm

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.util.TranslationEngine

class MMEngine : TranslationEngine(
    name = "MyMemory",
    defaultUrl = "https://api.mymemory.translated.net",
    urlModifiable = false,
    apiKeyState = ApiKeyState.OPTIONAL
) {
    override fun create(): TranslationEngine = apply {
        apiHelper = MMHelper()
    }
}
