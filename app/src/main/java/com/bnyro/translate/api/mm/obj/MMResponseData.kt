package com.bnyro.translate.api.mm.obj

import kotlinx.serialization.Serializable

@Serializable
data class MMResponseData(
    val match: Int = 0,
    val translatedText: String = "",
    val detectedLanguage: String? = null
)
