package com.bnyro.translate.api.deepl

import com.bnyro.translate.constants.ApiKeyState
import com.bnyro.translate.util.TranslationEngine

class DeeplEngine : TranslationEngine(
    name = "DeepL",
    defaultUrl = "https://api-free.deepl.com",
    urlModifiable = false,
    apiKeyState = ApiKeyState.REQUIRED
)
