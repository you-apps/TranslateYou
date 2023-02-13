package com.bnyro.translate.obj

import kotlinx.serialization.Serializable

@Serializable
data class DetectedLanguage(
    val confidence: Int? = null,
    val language: String? = null
)
