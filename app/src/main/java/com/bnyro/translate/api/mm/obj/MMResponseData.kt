package com.bnyro.translate.api.mm.obj

import kotlinx.serialization.Serializable

@Serializable
data class MMResponseData(
    val match: Float = 0f,
    val translatedText: String = "",
    val detectedLanguage: String? = null
)
