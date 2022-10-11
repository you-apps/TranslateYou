package com.bnyro.translate.api.lt

import com.bnyro.translate.constants.ApiKeyState
import com.bnyro.translate.util.TranslationEngine

class LTEngine : TranslationEngine(
    id = 0,
    name = "LibreTranslate",
    defaultUrl = "https://libretranslate.de",
    urlModifiable = true,
    apiKeyState = ApiKeyState.OPTIONAL
)
