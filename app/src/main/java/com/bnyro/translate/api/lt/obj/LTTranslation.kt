package com.bnyro.translate.api.lt.obj

import kotlinx.serialization.Serializable

@Serializable
data class LTTranslation(
    val translatedText: String = "",
    val detectedLanguage: LTDetectedLanguage? = null
)
