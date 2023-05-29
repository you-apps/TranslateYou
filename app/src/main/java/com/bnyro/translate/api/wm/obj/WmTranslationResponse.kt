package com.bnyro.translate.api.wm.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WmTranslationResponse(
    val model: String,
    @SerialName("sourcelanguage") val sourceLanguage: String,
    @SerialName("targetlanguage") val targetLanguage: String,
    val translation: String,
    @SerialName("translationtime") val translationTime: Float
)
