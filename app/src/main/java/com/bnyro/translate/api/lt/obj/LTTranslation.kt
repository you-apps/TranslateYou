package com.bnyro.translate.api.lt.obj

import com.bnyro.translate.obj.DetectedLanguage

data class LTTranslation(
    val translatedText: String = "",
    val detectedLanguage: DetectedLanguage? = null
)
