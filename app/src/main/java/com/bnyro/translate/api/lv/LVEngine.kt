package com.bnyro.translate.api.lv

import com.bnyro.translate.constants.ApiKeyState
import com.bnyro.translate.util.TranslationEngine

class LVEngine : TranslationEngine(
    name = "LingvaTranslate",
    defaultUrl = "https://lingva.ml",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED
)
