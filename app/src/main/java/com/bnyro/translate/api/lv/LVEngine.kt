package com.bnyro.translate.api.lv

import com.bnyro.translate.constants.ApiKeyState
import com.bnyro.translate.util.TranslationEngine

class LVEngine : TranslationEngine(
    id = 1,
    name = "LingvaTranslate",
    defaultUrl = "https://lingva.ml",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED
)
